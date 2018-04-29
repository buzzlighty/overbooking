package com.keypr.overbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author kyrylo.bazarov@avid.com
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongInputException extends RuntimeException {
    public WrongInputException(String message) {
        super(message);
    }
}
