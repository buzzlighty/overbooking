package com.keypr.overbooking.service;

import com.keypr.overbooking.dao.document.Reservation;
import com.keypr.overbooking.dto.BookingDto;

import java.util.List;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
  *
 *
 * Service to make bookings
 *
 */
public interface BookingService {

    /**
     * Creates booking in underlying storage
     *
     */
    void createBooking(BookingDto bookingDto);

    /**
     * Returns all bookings
     */
    List<Reservation> getAllBookings();
}
