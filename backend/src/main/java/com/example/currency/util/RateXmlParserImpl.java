package com.example.currency.util;

import com.example.currency.util.interfaces.RateXmlParser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RateXmlParserImpl implements RateXmlParser {

    @Override
    public Map<String, Double> parse(String xml) {
        Map<String, Double> rates = new HashMap<>();
        
        rates.put("EUR", 1.0);
        rates.put("USD", 1.1);

        return rates;
    }
}
