package com.keypr.overbooking.utils;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 */
public class DateHelperTest {

    @Test
    public void rangeIn() {
        LocalDate before = LocalDate.of(2018, 5, 1);
        LocalDate after = LocalDate.of(2018, 6, 1);

        assertThat(DateHelper.rangeIn(before, after, 20, 5)).isFalse();
        assertThat(DateHelper.rangeIn(before, after, 35, 5)).isTrue();
        assertThat(DateHelper.rangeIn(after, before, 30, 5)).isFalse();
        assertThat(DateHelper.rangeIn(LocalDate.now(), LocalDate.now(), 30, 0)).isTrue();
    }
}
