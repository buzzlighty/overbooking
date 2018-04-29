package com.keypr.overbooking;

import com.mongodb.MongoClient;
import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.core.MongoTemplate;
import redis.embedded.RedisServer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author kyrylo.bazarov@avid.com
 */
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableSwagger2.class))
public class ContextConfig {

    static {
        System.setProperty("MONGO_IP", "localhost");
        System.setProperty("MONGO_PORT", "27117");
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

    @Bean
    public MongoTemplate mongoTemplate() throws IOException {
        EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
        mongo.setBindIp("localhost");
        MongoClient mongoClient = mongo.getObject();
        return new MongoTemplate(mongoClient, "booking");
    }

    @PreDestroy
    public void stop() {
        redisServer.stop();
    }

}
