package com.crewmeister.cmcodingchallenge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyExchangeRateDto {

    private String baseCurrency;
    private String dateRate;
    private double rate;
    private String currencyCode;
}
