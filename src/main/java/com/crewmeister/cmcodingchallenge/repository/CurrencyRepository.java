package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Integer> {
    Optional<Currency> findByCode(String code);
    Collection<Currency> findAllByName(String name);
    Collection<Currency> findAllByCountryName(String name);
}
