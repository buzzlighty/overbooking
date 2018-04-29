package api;

import com.keypr.overbooking.OverbookingApplication;
import com.keypr.overbooking.dao.ConfigRepository;
import com.keypr.overbooking.dao.document.Config;
import com.keypr.overbooking.dao.document.Reservation;
import com.keypr.overbooking.dto.BookingDto;
import com.keypr.overbooking.dto.ConfigDto;
import com.keypr.overbooking.utils.DateHelper;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

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

        assertThat(restTemplate.postForEntity(serviceUrl, oneDay, null).getStatusCode())
                .as("On empty config should return 400 (NOT_FOUND)")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void booking() {
        BookingDto oneDay = new BookingDto("test", "test", LocalDate.now(), LocalDate.now());

        assertThat(restTemplate.postForEntity(serviceUrl, oneDay, null).getStatusCode())
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
    public void booking_parallel() {
        Config config = new Config(100, 20);
        config.setId(ConfigRepository.ID);

        mongoTemplate.insert(config);

        //todo
    }

    @Test
    public void booking_onReachedLimit_shouldReturn400() {
        BookingDto oneDay = new BookingDto("test", "test", LocalDate.now(), LocalDate.now());

        for(int i = 0; i < 11; i++ ){
            assertThat(restTemplate.postForEntity(serviceUrl, oneDay, null).getStatusCode())
                    .as("On present config should return 200")
                    .isEqualTo(HttpStatus.OK);
        }

        assertThat(restTemplate.postForEntity(serviceUrl, oneDay, null).getStatusCode())
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
    public void booking_overBookingChange_shouldIncreaseLimit() {
        BookingDto oneDay = new BookingDto("test", "test", LocalDate.now(), LocalDate.now());

        for(int i = 0; i < 11; i++ ){
           makeBooking(oneDay); //make booking for 11 times
        }

        assertThat(restTemplate.postForEntity(serviceUrl, oneDay, null).getStatusCode())
                .as("On reaching limit should return 400")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        restTemplate.postForEntity(configServiceUrl, new ConfigDto(10, 20), null); //increase overbooking

        assertThat(restTemplate.postForEntity(serviceUrl, oneDay, null).getStatusCode())
                .as("On present config should return 200")
                .isEqualTo(HttpStatus.OK); //no more 400 after limit is reached to 12

        assertThat(restTemplate.postForEntity(serviceUrl, oneDay, null).getStatusCode())
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

    private void makeBooking(BookingDto bookingDto) {
        restTemplate.postForEntity(serviceUrl, bookingDto, null);
    }
}
