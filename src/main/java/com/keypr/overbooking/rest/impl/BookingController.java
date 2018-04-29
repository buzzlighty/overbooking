package com.keypr.overbooking.rest.impl;

import com.keypr.overbooking.dao.document.Reservation;
import com.keypr.overbooking.dto.BookingDto;
import com.keypr.overbooking.exception.WrongInputException;
import com.keypr.overbooking.rest.BookingApi;
import com.keypr.overbooking.service.BookingService;
import com.keypr.overbooking.utils.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 * Rest controller for booking operations
 */
@RestController
public class BookingController implements BookingApi {

    @Autowired
    private BookingService bookingService;

    @Override
    @RequestMapping(path = "/booking", method = RequestMethod.POST)
    public void book(@RequestBody BookingDto booking) {
        if(!DateHelper.rangeIn(booking.getArrivalDate(), booking.getDepartureDate(), 31, 0)){
            throw new WrongInputException("Range between arrival and departure can't be more then 30 days and less then 1");
        }
        bookingService.createBooking(booking);
    }

    @Override
    @RequestMapping(path = "/booking", method = RequestMethod.GET)
    public List<Reservation> book() {
        return bookingService.getAllBookings();
    }

}
