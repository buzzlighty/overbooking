package com.keypr.overbooking.rest.impl;

import com.keypr.overbooking.ContextConfig;
import com.keypr.overbooking.dto.BookingDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertTrue;


/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContextConfig.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookingControllerTest {

    @Autowired
    private BookingController controller;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void book_onBookingWithBadDates_shouldReturn404() {

        Date arrival = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date departure = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        BookingDto bad = new BookingDto("test", "test", arrival, departure);

        assertTrue(this.restTemplate.postForEntity("http://localhost:"+ port + "/app/booking", bad, null)
                .getStatusCode().is4xxClientError());
    }
}
