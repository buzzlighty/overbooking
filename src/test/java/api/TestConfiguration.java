package api;

import com.mongodb.Mongo;
import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import redis.embedded.RedisServer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 */
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableSwagger2.class))
public class TestConfiguration {

    static {
        System.setProperty("REDIS_IP", "localhost");
        System.setProperty("REDIS_PORT", "7379");
        System.setProperty("LOGGING_LEVEL_ROOT", "WARN");
    }

    private RedisServer redisServer;

    @PostConstruct
    public void init() throws IOException {
        redisServer = new RedisServer(7379);
        redisServer.start();
    }

    @PreDestroy
    public void stop() {
        redisServer.stop();
    }

    @Bean
    public Mongo mongo() throws IOException {
        return new EmbeddedMongoBuilder().build();
    }

}
