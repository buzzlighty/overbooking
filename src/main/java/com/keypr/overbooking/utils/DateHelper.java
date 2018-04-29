package com.keypr.overbooking.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
  *
 *
 * Simple helper to iterate over date range
 */
public class DateHelper {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private LocalDate startDate;
    private LocalDate endDate;

    private DateHelper(){
    }

    public static Stream<LocalDate> between(LocalDate start, LocalDate end) {
        DateHelper dateRange = new DateHelper();
        dateRange.startDate = start;
        dateRange.endDate = end;

        return dateRange.stream();
    }

    public static boolean rangeIn(LocalDate from, LocalDate to, long max, long min) {
        long range = ChronoUnit.DAYS.between(from, to);
        return range >= min && range < max;
    }

    private Stream<LocalDate> stream() {
        return Stream.iterate(startDate, d -> d.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1);
    }
}
