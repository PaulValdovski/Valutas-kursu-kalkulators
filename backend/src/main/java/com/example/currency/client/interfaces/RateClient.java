package com.example.currency.client.interfaces;

import java.time.LocalDate;
import java.util.Map;

public interface RateClient {
    Map<String, Double> fetchRates(LocalDate date);
}
