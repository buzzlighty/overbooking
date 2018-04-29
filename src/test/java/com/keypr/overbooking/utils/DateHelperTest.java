package com.keypr.overbooking.utils;

import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 */
public class DateHelperTest {

    @Test
    public void after() {
        LocalDate before = LocalDate.of(2010, 1, 1);
        LocalDate after = LocalDate.of(2020, 1, 1);

        assertThat(DateHelper.after(after, before))
                .as("If second date is sooner, should return false")
                .isFalse();

        assertThat(DateHelper.after(before, after))
                .as("If first date is sooner, should return true")
                .isTrue();

        assertThat(DateHelper.after(after, after))
                .as("If same day, should return true")
                .isTrue();
    }
}
