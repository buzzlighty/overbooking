package com.keypr.overbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
  *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservationLimitReachedException extends RuntimeException {
    public ReservationLimitReachedException() {
        super("Reservation limit reached");
    }
}
