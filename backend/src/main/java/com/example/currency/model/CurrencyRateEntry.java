package com.example.currency.model;

import java.time.LocalDate;

public class CurrencyRateEntry {
    private final LocalDate date;
    private final double rate;

    public CurrencyRateEntry(LocalDate date, double rate) {
        this.date = date;
        this.rate = rate;
    }

    public LocalDate getDate() { return date; }
    public double getRate() { return rate; }
}
