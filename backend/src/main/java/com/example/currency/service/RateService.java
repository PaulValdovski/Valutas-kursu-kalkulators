package com.example.currency.service;

import com.example.currency.client.BankLvClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class RateService {

    private final BankLvClient bankLvClient;

    public RateService(BankLvClient bankLvClient) {
        this.bankLvClient = bankLvClient;
    }

    public double getRate(String from, String to, LocalDate date) {
        Map<String, Double> rates = bankLvClient.fetchRates(date);
        double fromRate = rates.get(from);
        double toRate = rates.get(to);
        return toRate / fromRate;
    }

    public Map<String, Double> getRates(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }

        Map<String, Double> rates = bankLvClient.fetchRates(date);

        if (rates == null || rates.isEmpty()) {
            throw new IllegalArgumentException("No rates available for the given date");
        }

        return rates;
    }
}
