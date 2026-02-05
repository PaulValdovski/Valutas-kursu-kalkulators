package com.example.currency.service;

import com.example.currency.model.CurrencyRateEntry;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyHistoryService {

    private final RateService rateService;

    public CurrencyHistoryService(RateService rateService) {
        this.rateService = rateService;
    }

    public List<CurrencyRateEntry> getHistory(String fromCurrency, String toCurrency, int days) {
        List<CurrencyRateEntry> history = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            Map<String, Double> rates = rateService.getRates(date);

            Double fromRate = rates.get(fromCurrency);
            Double toRate = rates.get(toCurrency);

            if (fromRate == null || toRate == null) {
                continue;
            }

            double convertedRate = toRate / fromRate;
            history.add(new CurrencyRateEntry(date, convertedRate));
        }

        return history;
    }
}
