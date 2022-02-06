package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    Collection<CurrencyRate> findAllByCurrency(Currency currency);

    Collection<CurrencyRate> findAllByRateDate(Date date);

    CurrencyRate findByRateDateAndCurrency(Date date, Currency currency);

    boolean existsByCurrency(Currency currency);
    boolean existsByCurrency_CodeAndRateDate(String currency,Date date);
}
