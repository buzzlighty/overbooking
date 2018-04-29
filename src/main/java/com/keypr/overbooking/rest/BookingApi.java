package com.keypr.overbooking.rest;

import com.keypr.overbooking.dao.document.Reservation;
import com.keypr.overbooking.dto.BookingDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

/**
 * @author kyrylo.bazarov@avid.com
 */
public interface BookingApi {

    @ApiOperation(value = "Create booking for specified dates", tags = "Booking")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Reservation has been saved"),
            @ApiResponse(code = 400, message = "Reservation limit is reached"),
            @ApiResponse(code = 404, message = "Overbooking configuration is missing")
    })
    void book(BookingDto booking);

    @ApiOperation(value = "Get all bookings", tags = "Booking")
    @ApiResponse(code = 200, message = "OK", response = Reservation.class, responseContainer = "List")
    List<Reservation> book();

}
