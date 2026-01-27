package com.example.currency.model;

import java.time.LocalDate;

public class ConversionResult {

    private final String fromCurrency;
    private final String toCurrency;
    private final double originalAmount;
    private final double convertedAmount;
    private final LocalDate rateDate;

    public ConversionResult(String fromCurrency, String toCurrency, double originalAmount,
                            double convertedAmount, LocalDate rateDate) {
        if (fromCurrency == null || fromCurrency.isEmpty()) {
            throw new IllegalArgumentException("From currency cannot be null");
        }
        if (toCurrency == null || toCurrency.isEmpty()) {
            throw new IllegalArgumentException("To currency cannot be null");
        }
        if (rateDate == null) {
            throw new IllegalArgumentException("Rate date cannot be null");
        }
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
        this.rateDate = rateDate;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }

    public LocalDate getRateDate() {
        return rateDate;
    }

    @Override
    public String toString() {
        return "ConversionResult{" +
                "fromCurrency='" + fromCurrency + '\'' +
                ", toCurrency='" + toCurrency + '\'' +
                ", originalAmount=" + originalAmount +
                ", convertedAmount=" + convertedAmount +
                ", rateDate=" + rateDate +
                '}';
    }
}
