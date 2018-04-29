package com.keypr.overbooking.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 *  Common Spring java configuration for misc beans and properties.
 */
@Configuration
public class CommonConfiguration {

    @Value("${REDIS_IP}")
    private String redisHost;

    @Value("${REDIS_PORT}")
    private int redisPort;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
        config.useSingleServer()
                .setAddress("redis://" + this.redisHost + ":" + this.redisPort);

        return Redisson.create(config);
    }


}
