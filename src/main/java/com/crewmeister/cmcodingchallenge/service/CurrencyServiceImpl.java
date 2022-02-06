package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.config.EndPoints;
import com.crewmeister.cmcodingchallenge.exception.BusinessException;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.dto.CountryTempDto;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final ResourceService resourceService;
    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(ResourceService resourceService, CurrencyRepository currencyRepository) {
        this.resourceService = resourceService;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<String> getAllCurrency() {
        Collection<Currency> countryStream = fetchAll();
        return countryStream.stream().map(Currency::getCode).collect(Collectors.toList());

    }
    // todo unit test
    private Currency fillProperty(CountryTempDto countryTmp) {
        Currency country = new Currency();
//        AED United Arab Emirates dirham (United Arab Emirates)
        if (countryTmp.getNodeTitle() != null && !countryTmp.getNodeTitle().isEmpty()) {
            String tmp = countryTmp.getNodeTitle();
            country.setCode(tmp.substring(0, 3));
            int endName = tmp.indexOf("(");
            if (endName == -1) {
                endName = tmp.indexOf(",");
                if (endName == -1) {
                    endName = 3;
                }
            }
            country.setName(tmp.substring(3, endName).trim());
            country.setCountryName(tmp.substring(endName + 1, tmp.lastIndexOf(")") - 1).trim());
        }

        return country;
    }

    @Override
    public void save(Currency currency) {
        currencyRepository.save(currency);
    }
    // todo unit test
    @Override
    public void save(Collection<Currency> currency) {
        currencyRepository.saveAll(currency);
    }

    @Override
    public boolean isExist(String code) {
        return false;
    }

    @Override
    public Collection<Currency> fetchAll() {
        if (currencyRepository.count() == 0) {
            return initCurrencyTable();
        } else {
            return currencyRepository.findAll();
        }
    }

    private List<Currency> initCurrencyTable() {
        CountryTempDto[] allCurrency = resourceService.getMethod(EndPoints.ALL_CURRENCY, CountryTempDto[].class);
        if (allCurrency == null) {
            throw new BusinessException("Connection with Api server fail");
        }
        List<Currency> currencyList = Arrays.stream(allCurrency).map(c -> fillProperty(c)).collect(Collectors.toList());
        save(currencyList);
        return currencyList;
    }

    @Override
    public Collection<Currency> findByName(String name) {
        return currencyRepository.findAllByName(name);
    }

    @Override
    public Currency findByCode(String code) {
        // todo unit test
        if (currencyRepository.count() == 0) {
            initCurrencyTable();
        }
        Optional<Currency> currency = currencyRepository.findByCode(code.toUpperCase());
        if (currency.isEmpty()) {
            throw new BusinessException("Currency Not Found");
        }
        return currency.get();
    }
    // todo unit test
    @Override
    public Collection<Currency> findByCountryName(String name) {
        return currencyRepository.findAllByCountryName(name);
    }
}
