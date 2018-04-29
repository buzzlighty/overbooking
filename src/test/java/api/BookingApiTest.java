package api;

import com.keypr.overbooking.OverbookingApplication;
import com.keypr.overbooking.dao.ConfigRepository;
import com.keypr.overbooking.dao.document.Config;
import com.keypr.overbooking.dao.document.Reservation;
import com.keypr.overbooking.dto.BookingDto;
import com.keypr.overbooking.dto.ConfigDto;
import com.keypr.overbooking.utils.DateHelper;
import org.apache.tomcat.jni.Local;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OverbookingApplication.class, TestConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingApiTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String serviceUrl;
    private String configServiceUrl;

    @Before
    public void init() {
        serviceUrl =  "http://localhost:"+ port + "/app/booking";
        configServiceUrl = "http://localhost:" + port + "/app/configuration";

        Config config = new Config(10, 10);
        config.setId(ConfigRepository.ID);

        mongoTemplate.insert(config);
    }

    @After
    public void cleanup() {
        mongoTemplate.remove(Config.class).findAndRemove();
        mongoTemplate.remove(Reservation.class).findAndRemove();
    }

    @Test
    public void book_onEmptyConfig_shouldReturn400() {
        this.cleanup();

        BookingDto oneDay = new BookingDto("test", "test", LocalDate.now(), LocalDate.now());

        assertThat(makeBooking(oneDay).getStatusCode())
                .as("On empty config should return 400 (NOT_FOUND)")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void booking() {
        BookingDto oneDay = new BookingDto("test", "test", LocalDate.now(), LocalDate.now());

        assertThat(makeBooking(oneDay).getStatusCode())
                .as("On present config should return 200")
                .isEqualTo(HttpStatus.OK);

        Reservation actual = mongoTemplate.findAll(Reservation.class).get(0);

        assertThat(actual.getDay())
                .as("Saved reservation should have proper day")
                .isEqualTo(LocalDate.now().format(DateHelper.formatter));

        assertThat(actual.getLimit())
                .as("Limit should be increased by 1")
                .isEqualTo(1);
    }

    @Test
    public void booking_parallel() throws InterruptedException {
        saveConfiguration(new ConfigDto(100, 20));

        ExecutorService executor = Executors.newFixedThreadPool(120);

        LocalDate start1 = LocalDate.of(2018, 1, 1);
        LocalDate end1 = LocalDate.of(2018,1, 10);

        LocalDate start2 = LocalDate.of(2018, 1, 5);
        LocalDate end2 = LocalDate.of(2018, 1, 15);

        LocalDate start3 = LocalDate.of(2018, 1, 11);
        LocalDate end3 = LocalDate.of(2018, 1, 20);

        List<Callable<ResponseEntity<Object>>> range = IntStream.range(0, 120).mapToObj(i -> {
            if(i >= 0 && i < 40) {
                return new BookingDto("test", "test", start1, end1);
            } else if (i >= 40 && i < 80) {
                return new BookingDto("test", "test", start2, end2);
            } else {
                return new BookingDto("test", "test", start3, end3);
            }
        }).map(bookingDto -> (Callable<ResponseEntity<Object>>) () -> makeBooking(bookingDto)).collect(Collectors.toList());

        executor.invokeAll(range);
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        List<Reservation> reservations = mongoTemplate.findAll(Reservation.class);

        assertThat(reservations.size()).
                as("Reservations count should be equal to range sum")
                .isEqualTo(20);

        reservations.forEach(reservation -> {
            LocalDate actual = LocalDate.parse(reservation.getDay(), DateHelper.formatter);
            if (actual.isBefore(start2) || actual.isAfter(end2)) {
                assertThat(reservation.getLimit()).isEqualTo(40);
            } else {
                assertThat(reservation.getLimit()).isEqualTo(80);
            }
        });
    }

    @Test
    public void booking_onReachedLimit_shouldReturn400() {
        BookingDto oneDay = new BookingDto("test", "test", LocalDate.now(), LocalDate.now());

        for(int i = 0; i < 11; i++ ){
            assertThat(makeBooking(oneDay).getStatusCode())
                    .as("On present config should return 200")
                    .isEqualTo(HttpStatus.OK);
        }

        assertThat(makeBooking(oneDay).getStatusCode())
                .as("On reaching limit should return 400")
                .isEqualTo(HttpStatus.BAD_REQUEST);


        Reservation actual = mongoTemplate.findAll(Reservation.class).get(0);

        assertThat(actual.getDay())
                .as("Saved reservation should have proper day")
                .isEqualTo(LocalDate.now().format(DateHelper.formatter));

        assertThat(actual.getLimit())
                .as("Limit should be equal to 11")
                .isEqualTo(11);
    }

    @Test
    public void booking_wrongDate_shouldReturn400() {
        BookingDto bad = new BookingDto("test", "test", LocalDate.of(2018,2,1),
                LocalDate.of(2018,1,1));

        assertThat(makeBooking(bad).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void booking_longDateRange_shouldReturn400() {
        BookingDto bad = new BookingDto("test", "test", LocalDate.of(2018,1,1),
                LocalDate.of(2018,2,1));

        assertThat(makeBooking(bad).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void booking_overBookingChange_shouldIncreaseLimit() {
        BookingDto oneDay = new BookingDto("test", "test", LocalDate.now(), LocalDate.now());

        for(int i = 0; i < 11; i++ ){
           makeBooking(oneDay); //make booking for 11 times
        }

        assertThat(makeBooking(oneDay).getStatusCode())
                .as("On reaching limit should return 400")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        saveConfiguration(new ConfigDto(10, 20)); //increase overbooking (12)

        assertThat(makeBooking(oneDay).getStatusCode())
                .as("On present config should return 200")
                .isEqualTo(HttpStatus.OK); //no more 400 after limit is reached to 12

        assertThat(makeBooking(oneDay).getStatusCode())
                .as("On reaching limit should return 400")
                .isEqualTo(HttpStatus.BAD_REQUEST); //now limit is reached again

        Reservation actual = mongoTemplate.findAll(Reservation.class).get(0);

        assertThat(actual.getDay())
                .as("Saved reservation should have proper day")
                .isEqualTo(LocalDate.now().format(DateHelper.formatter));

        assertThat(actual.getLimit())
                .as("Limit should be equal to 12")
                .isEqualTo(12);
    }

    private void saveConfiguration(ConfigDto configDto) {
        restTemplate.postForEntity(configServiceUrl, configDto, null);
    }

    private ResponseEntity<Object> makeBooking(BookingDto bookingDto) {
        return restTemplate.postForEntity(serviceUrl, bookingDto, null);
    }
}
