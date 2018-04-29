package api;

import com.keypr.overbooking.OverbookingApplication;
import com.keypr.overbooking.dao.ConfigRepository;
import com.keypr.overbooking.dao.document.Config;
import com.keypr.overbooking.dao.document.Reservation;
import com.keypr.overbooking.dto.ConfigDto;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author kyrylo.bazarov@avid.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OverbookingApplication.class, TestConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfigApiTest {


    @Autowired
    private MongoTemplate mongoTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String serviceUrl;

    @Before
    public void init() {
        serviceUrl = "http://localhost:" + port + "/app/configuration";

    }

    @After
    public void cleanup() {
        mongoTemplate.remove(Config.class).findAndRemove();
        mongoTemplate.remove(Reservation.class).findAndRemove();
    }

    @Test
    public void config() {
        Config config = new Config(10, 10);
        config.setId(ConfigRepository.ID);

        assertThat(restTemplate.postForEntity(serviceUrl, new ConfigDto(10, 10), null)
                               .getStatusCode())
                .as("Should return 200 on valid payload")
                .isEqualTo(HttpStatus.OK);

        Config actual = mongoTemplate.findAll(Config.class).get(0);

        assertThat(actual.getId())
                .as("Should save config with proper ID")
                .isEqualTo(config.getId());

        assertThat(actual.getRooms())
                .as("Should save config with proper Rooms")
                .isEqualTo(config.getRooms());

        assertThat(actual.getOverbooking())
                .as("Should save config proper Overbooking")
                .isEqualTo(config.getOverbooking());
    }

    @Test
    public void config_onMissingConfig_shouldReturn404() {
        assertThat(restTemplate.getForEntity(serviceUrl, null)
                .getStatusCode())
                .as("Should return 404 when config is missing")
                .isEqualTo(HttpStatus.NOT_FOUND);

    }
}
