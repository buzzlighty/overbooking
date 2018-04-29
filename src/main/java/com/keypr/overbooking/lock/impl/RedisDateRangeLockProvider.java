package com.keypr.overbooking.lock.impl;

import com.keypr.overbooking.lock.DateRangeLockProvider;
import com.keypr.overbooking.utils.DateRange;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.locks.Lock;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 *
 * Redis based distributed implementation of {@link DateRangeLockProvider}
 *
 */
@Component("redisDateRangeLock")
public class RedisDateRangeLockProvider implements DateRangeLockProvider {

    @Autowired
    private RedissonClient redisson;

    /**
     * Creates MultiLock based on {@link RedissonMultiLock}
     *
     */
    @Override
    public Lock getDateRangeLock(LocalDate from, LocalDate to) {
        RLock[] locks = DateRange.between(from, to)
                .map(localDate -> DateRange.formatter.format(localDate))
                .map(day -> redisson.getLock(day))
                .toArray(RLock[]::new);

        return new RedissonMultiLock(locks);
    }
}
