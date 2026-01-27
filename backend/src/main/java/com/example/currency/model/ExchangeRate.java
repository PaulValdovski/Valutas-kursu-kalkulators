package com.example.currency.model;

import java.time.LocalDate;

public class ExchangeRate {

    private final String currency;
    private final double rate;
    private final LocalDate date;

    public ExchangeRate(String currency, double rate, LocalDate date) {
        if (currency == null || currency.isEmpty()) {
            throw new IllegalArgumentException("Currency code cannot be null");
        }
        if (rate <= 0) {
            throw new IllegalArgumentException("Rate must be positive");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.currency = currency;
        this.rate = rate;
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public double getRate() {
        return rate;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "currency='" + currency + '\'' +
                ", rate=" + rate +
                ", date=" + date +
                '}';
    }
}
