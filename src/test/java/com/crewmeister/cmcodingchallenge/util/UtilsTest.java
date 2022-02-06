package com.crewmeister.cmcodingchallenge.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UtilsTest {

    @Test
    void convertStringDateValueToDate() {
        Date date = Utils.convertStringDateValueToDate("2022-02-04");
        Calendar instance = Calendar.getInstance();
        instance.set(2022, Calendar.FEBRUARY,4,0,0,0);
        instance.set(Calendar.MILLISECOND,0);
        Assertions.assertEquals(date, instance.getTime());
    }

    @Test
    void convertToDouble() {
    }

    @Test
    void getDataDaily() {
    }

    @Test
    void getDataMonthly() {
    }

    @Test
    void getDataAnnually() {
    }
}