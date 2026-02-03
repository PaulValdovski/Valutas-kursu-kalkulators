package com.example.currency.service;

import com.example.currency.model.ConversionResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class ConversionService {

    public ConversionResult convert(double amount,
                                    String fromCurrency,
                                    String toCurrency,
                                    LocalDate rateDate,
                                    Map<String, Double> rates) {

        if (rates == null || rates.isEmpty()) {
            throw new IllegalArgumentException("Currency not available for the given date");
        }

        Double fromRate = rates.get(fromCurrency);
        Double toRate = rates.get(toCurrency);

        if (fromRate == null || toRate == null) {
            throw new IllegalArgumentException("Currency not available for the given date");
        }

        double convertedAmount = amount * (toRate / fromRate);

        return new ConversionResult(
                fromCurrency,
                toCurrency,
                amount,
                convertedAmount,
                rateDate
        );
    }
}
