package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.config.ConstantVal;
import com.crewmeister.cmcodingchallenge.config.EndPoints;
import com.crewmeister.cmcodingchallenge.exception.BusinessException;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.CurrencyRate;
import com.crewmeister.cmcodingchallenge.model.dto.CurrencyExchangeRateDto;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRateRepository;
import com.crewmeister.cmcodingchallenge.util.Utils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {
    private final CurrencyService currencyService;
    private final ResourceService resourceService;
    private final CurrencyRateRepository currencyRateRepository;

    public CurrencyRateServiceImpl(CurrencyService currencyService, ResourceService resourceService, CurrencyRateRepository currencyRateRepository) {
        this.currencyService = currencyService;
        this.resourceService = resourceService;
        this.currencyRateRepository = currencyRateRepository;
    }

    @Override
    public Collection<CurrencyExchangeRateDto> getAllRateByCurrencyAndDate(String currency, String date) {
        Currency code = currencyService.findByCode(currency);
        Date rateDate = Utils.convertStringDateValueToDate(date);
        if (checkExistCurrencyRateDataInDb(code.getCode(), rateDate)) {
            return convertToCurrencyExchangeRateDto(List.of(currencyRateRepository.findByRateDateAndCurrency(rateDate, code)));
        }
        Collection<CurrencyExchangeRateDto> rateByCurrency = getAllRateByCurrency(currency);
        if (rateByCurrency != null && !rateByCurrency.isEmpty()) {
            return rateByCurrency.stream().filter(c -> c.getDateRate().equals(date)).collect(Collectors.toList());
        }
        throw new BusinessException("Date Not Found !...");
    }

    @Override
    public Collection<CurrencyExchangeRateDto> calculateAmountByCurrencyRateByDate(String currency, String date, Double amount) {
        Collection<CurrencyExchangeRateDto> allRateByCurrencyAndDate = getAllRateByCurrencyAndDate(currency, date);
        if (allRateByCurrencyAndDate != null) {
            allRateByCurrencyAndDate.forEach(c -> c.setRate(c.getRate() * amount));
            return allRateByCurrencyAndDate;
        }
        return null;
    }

    @Override
    public Collection<CurrencyExchangeRateDto> getAllRateByCurrency(String currency) {

        Currency code = currencyService.findByCode(currency);
        if (currencyRateRepository.count() == 0 || !currencyRateRepository.existsByCurrency(code)) {
            List<CurrencyExchangeRateDto> currencyExchangeRateDtoList = getFromResource(code);
            List<CurrencyRate> currencyRates = convertToCurrencyRate(code, currencyExchangeRateDtoList);
            saveAll(currencyRates);
            return currencyExchangeRateDtoList;
        } else {
            Collection<CurrencyRate> allRateByCurrency = currencyRateRepository.findAllByCurrency(code);
            return convertToCurrencyExchangeRateDto(allRateByCurrency);
        }
    }

    private List<CurrencyExchangeRateDto> getFromResource(Currency code) {
        String response = getRateCurrency(code);
        return convertDateToList(response);
    }

    private boolean checkExistCurrencyRateDataInDb(String currencyCode, Date rateDate) {
        if (currencyRateRepository.count() == 0) {
            return false;
        }
        if (currencyCode == null || currencyCode.isEmpty()) {
            throw new BusinessException("Currency Code parameter is null");
        }
        if (rateDate == null) {
            throw new BusinessException("Rate Date parameter is null");
        }
        return currencyRateRepository.existsByCurrency_CodeAndRateDate(currencyCode, rateDate);
    }

    private Collection<CurrencyExchangeRateDto> convertToCurrencyExchangeRateDto(Collection<CurrencyRate> allRateByCurrency) {
        return allRateByCurrency.stream().map(c ->
                new CurrencyExchangeRateDto(
                        ConstantVal.BASE_CURRENCY,
                        c.getRateDate().toString(),
                        c.getRate(),
                        c.getCurrency().getCode())).collect(Collectors.toList());
    }

    private void saveAll(List<CurrencyRate> currencyRates) {
        currencyRateRepository.saveAll(currencyRates);
    }

    private List<CurrencyRate> convertToCurrencyRate(Currency code, List<CurrencyExchangeRateDto> currencyExchangeRateDtoList) {
        List<CurrencyRate> currencyRates = new ArrayList<>(currencyExchangeRateDtoList.size());
        currencyExchangeRateDtoList.forEach(rateDto -> {
            Date rateDate = Utils.convertStringDateValueToDate(rateDto.getDateRate());
            currencyRates.add(new CurrencyRate(
                    null,
                    rateDate,
                    rateDto.getRate(),
                    code));
        });
        return currencyRates;
    }


    private List<CurrencyExchangeRateDto> convertDateToList(String body) {

        List<CurrencyExchangeRateDto> tmp = null;
        if (body != null) {
            tmp = new ArrayList<>();
            String[] rows = body.split("\n");
            if (rows.length > 5) {
                String destinationCurrency = "";
                Optional<String> unit = Arrays.stream(rows).filter(c -> c.startsWith("unit")).findFirst();
                if (unit.isPresent()) {
                    destinationCurrency = unit.get().split(",")[1];
                }
                for (int i = 5; i < rows.length - 2; i++) {
                    String col = rows[i];
                    if (col.toLowerCase().contains("general:")) {
                        break;
                    }
                    String[] columns = col.split(",");
                    if (columns.length >= 2) {
                        //1999-01-01
                        String date = columns[0];
                        double rate = columns[1].equals(".") ? 0 : Utils.convertToDouble(columns[1]);
                        tmp.add(new CurrencyExchangeRateDto("EUR", date, rate, destinationCurrency));
                    }
                }
            }
        }
        return tmp;
    }

    private String getRateCurrency(Currency code) {
        String response = resourceService.getMethod(Utils.getDataDaily(code.getCode()), String.class);
        if (response == null || response.isEmpty()) {
            throw new BusinessException("Check Your Request !!!  ");
        }
        return response;
    }
}
