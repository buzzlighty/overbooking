package com.keypr.overbooking.dao;

import com.keypr.overbooking.dao.document.Reservation;

import java.util.List;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 * Dao for making reservations
 */
public interface BookingDao {

    /**
     * Returns if there're days where reservation limit is greater or equal to provided in parameter
     *
     */
    boolean isDateRangeInLimit(int limit, String... days);

    /**
     * Increments reservation limit by 1 for all specified days
     *
     */
    void incrementLimitForDateRange(String ... days);

    /**
     * Returns all reservations
     *
     */
    List<Reservation> findAll();
}
