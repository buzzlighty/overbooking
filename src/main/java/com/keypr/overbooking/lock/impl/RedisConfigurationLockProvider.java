package com.keypr.overbooking.lock.impl;

import com.keypr.overbooking.lock.ConfigurationLockProvider;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 *
 * Redis based implementation of {@link ConfigurationLockProvider}
 */
@Component("redisConfigLock")
public class RedisConfigurationLockProvider implements ConfigurationLockProvider {

    @Autowired
    private RedissonClient redisson;

    @Override
    public ReadWriteLock getConfigurationLock() {
        return redisson.getReadWriteLock("settings");
    }
}
