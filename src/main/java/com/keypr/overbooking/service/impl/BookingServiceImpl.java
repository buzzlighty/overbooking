package com.keypr.overbooking.service.impl;

import com.keypr.overbooking.dao.BookingDao;
import com.keypr.overbooking.dao.ConfigRepository;
import com.keypr.overbooking.dao.document.Config;
import com.keypr.overbooking.dao.document.Reservation;
import com.keypr.overbooking.dto.BookingDto;
import com.keypr.overbooking.exception.MissingConfigurationException;
import com.keypr.overbooking.exception.ReservationLimitReachedException;
import com.keypr.overbooking.lock.ConfigurationLockProvider;
import com.keypr.overbooking.lock.DateRangeLockProvider;
import com.keypr.overbooking.service.BookingService;
import com.keypr.overbooking.utils.DateHelper;
import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 *
 * @inheritDoc
 */
@Component
public class BookingServiceImpl implements BookingService {

    @Autowired
    @Qualifier("redisDateRangeLock")
    private DateRangeLockProvider dateRangeLockProvider;

    @Autowired
    @Qualifier("redisConfigLock")
    private ConfigurationLockProvider configurationLockProvider;

    @Autowired
    private BookingDao reservationDao;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *
     * Gets lock for all days
     * Checks if all days not reached limit
     * Increments all days limit
     *
     */
    @Override
    public void createBooking(BookingDto bookingDto) {
        LocalDate from = bookingDto.getArrivalDate();
        LocalDate to = bookingDto.getDepartureDate();

        Lock lock = dateRangeLockProvider.getDateRangeLock(from, to);
        Lock configLock = configurationLockProvider.getConfigurationLock().readLock();

        try {
            configLock.lock();
            lock.lock();

            String[] days = DateHelper.between(from, to)
                    .map(localDate -> DateHelper.formatter.format(localDate))
                    .toArray(String[]::new);

            Config config = configRepository.findById(ConfigRepository.ID)
                    .orElseThrow(MissingConfigurationException::new);

            int limit = Math.round(config.getRooms() * ((config.getOverbooking() / 100f) + 1))  ;

            if(reservationDao.isDateRangeInLimit(limit, days)) {
                reservationDao.incrementLimitForDateRange(days);
            } else {
                throw new ReservationLimitReachedException();
            }
        } finally {
            configLock.unlock();
            lock.unlock();
        }
    }

    @Override
    public List<Reservation> getAllBookings() {
        return reservationDao.findAll();
    }
}