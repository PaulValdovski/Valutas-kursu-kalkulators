package com.example.currency.util.interfaces;

import java.util.Map;

public interface RateXmlParser {
    Map<String, Double> parse(String xml);
}
