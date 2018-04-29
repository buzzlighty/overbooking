package com.keypr.overbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
  *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MissingConfigurationException extends RuntimeException {
    public MissingConfigurationException() {
        super("Configuration is missing");
    }
}
