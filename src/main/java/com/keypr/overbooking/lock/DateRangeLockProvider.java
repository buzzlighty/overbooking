package com.keypr.overbooking.lock;

import java.time.LocalDate;
import java.util.concurrent.locks.Lock;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 *
 * Date range lock provider
 */
public interface DateRangeLockProvider {

    /**
     * Creates multi lock for each day in given data range.
     *
     * @param from startDate inclusive
     * @param to endDate inclusive
     * @return MultiLock for date range
     */
    Lock getDateRangeLock(LocalDate from, LocalDate to);

}
