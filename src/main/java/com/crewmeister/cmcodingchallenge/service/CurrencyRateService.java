package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.model.dto.CurrencyExchangeRateDto;

import java.util.Collection;

public interface CurrencyRateService {

    Collection<CurrencyExchangeRateDto> getAllRateByCurrency(String currency);

    Collection<CurrencyExchangeRateDto> getAllRateByCurrencyAndDate(String currency, String date);

    Collection<CurrencyExchangeRateDto> calculateAmountByCurrencyRateByDate(String currency, String date, Double amount);

}
