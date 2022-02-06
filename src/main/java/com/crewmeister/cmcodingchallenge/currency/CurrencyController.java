package com.crewmeister.cmcodingchallenge.currency;

import com.crewmeister.cmcodingchallenge.model.dto.CurrencyExchangeRateDto;
import com.crewmeister.cmcodingchallenge.service.CurrencyRateService;
import com.crewmeister.cmcodingchallenge.service.CurrencyService;
import com.crewmeister.cmcodingchallenge.service.CurrencyServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyRateService currencyRateService;

    public CurrencyController(CurrencyServiceImpl currencyService, CurrencyRateService currencyRateService) {
        this.currencyService = currencyService;
        this.currencyRateService = currencyRateService;
    }

    @GetMapping("/currencies")
    public ResponseEntity<ArrayList<CurrencyConversionRates>> getCurrencies() {
        ArrayList<CurrencyConversionRates> currencyConversionRates = new ArrayList<CurrencyConversionRates>();
        currencyConversionRates.add(new CurrencyConversionRates(2.5));

        return new ResponseEntity<ArrayList<CurrencyConversionRates>>(currencyConversionRates, HttpStatus.OK);
    }

    //all available currencies
    @GetMapping("/allcurrency")
    public ResponseEntity<Collection<String>> getAllCurrencies() {
        return new ResponseEntity<>(currencyService.getAllCurrency(), HttpStatus.OK);
    }

    //all EUR-FX exchange rates at all available dates as a collection
    @GetMapping("/ratecurrency")
    public ResponseEntity<Collection<CurrencyExchangeRateDto>> getAllEuroRates(@RequestParam(name = "currency")String currency) {

        return new ResponseEntity<>(currencyRateService.getAllRateByCurrency(currency), HttpStatus.OK);
    }

    //get the EUR-FX exchange rate at particular day
    @GetMapping("/ratecurrencybydate")
    public ResponseEntity<Collection<CurrencyExchangeRateDto>> getEuroRatesByDate(@RequestParam(name = "currency")String currency,@RequestParam(name = "date")String date) {
        return new ResponseEntity<>(currencyRateService.getAllRateByCurrencyAndDate(currency,date), HttpStatus.OK);
    }

    //get a foreign exchange amount for a given currency converted to EUR on a particular day
    @GetMapping("/calcratebydate")
    public ResponseEntity<Collection<CurrencyExchangeRateDto>> getAmountByCurrency(@RequestParam(name = "currency")String currency,@RequestParam(name = "amount") Double amount, @RequestParam(name = "date") String date) {
        return new ResponseEntity<>(currencyRateService.calculateAmountByCurrencyRateByDate(currency,date,amount), HttpStatus.OK);
    }
}
