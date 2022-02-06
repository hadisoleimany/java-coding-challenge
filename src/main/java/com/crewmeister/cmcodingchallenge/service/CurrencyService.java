package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.model.Currency;

import java.util.Collection;
import java.util.List;

public interface CurrencyService {

    void save(Currency currency);

    void save(Collection<Currency> currency);

    boolean isExist(String code);

    Collection<Currency> fetchAll();

    List<String> getAllCurrency();

    Collection<Currency> findByName(String name);

    Currency findByCode(String code);

    Collection<Currency> findByCountryName(String name);
}
