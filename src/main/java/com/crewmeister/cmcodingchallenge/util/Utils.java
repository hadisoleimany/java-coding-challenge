package com.crewmeister.cmcodingchallenge.util;

import com.crewmeister.cmcodingchallenge.config.ConstantVal;
import com.crewmeister.cmcodingchallenge.config.EndPoints;
import com.crewmeister.cmcodingchallenge.exception.BusinessException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static Date convertStringDateValueToDate(String rateDto) {
        try {
            return new SimpleDateFormat(ConstantVal.DATE_FORMAT).parse(rateDto);
        } catch (ParseException e) {
            throw new BusinessException(rateDto + " - " + e.getMessage());
        }
    }
    // todo unit test
    public static double convertToDouble(String amount) {
        try {
            return Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            throw new BusinessException(amount + " - " + e.getMessage());
        }
    }
    // todo unit test
    public static String getDataDaily(String code){
        return (EndPoints.BASE_URL_START +
                ConstantVal.DAILY + code.toUpperCase() + EndPoints.BASE_URL_END);
    }
    // todo unit test
    public static String getDataMonthly(String code){
        return (EndPoints.BASE_URL_START +
                ConstantVal.MONTHLY + code.toUpperCase() + EndPoints.BASE_URL_END);
    }
    // todo unit test
    public static String getDataAnnually(String code){
        return (EndPoints.BASE_URL_START +
                ConstantVal.YEARLY + code.toUpperCase() + EndPoints.BASE_URL_END);
    }
}
