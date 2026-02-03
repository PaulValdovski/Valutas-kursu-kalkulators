package com.example.currency.client;

import org.springframework.stereotype.Component;

import com.example.currency.client.interfaces.HttpClient;
import com.example.currency.client.interfaces.RateClient;
import com.example.currency.util.XmlParser;

import java.time.LocalDate;
import java.util.Map;

@Component
public class BankLvClient implements RateClient {

    private final XmlParser xmlParser;
    private final HttpClient httpClient;

    public BankLvClient(XmlParser xmlParser, HttpClient httpClient) {
        this.xmlParser = xmlParser;
        this.httpClient = httpClient;
    }

    @Override
    public Map<String, Double> fetchRates(LocalDate date) {
        String url = "https://www.bank.lv/vk/ecb.xml";
        
        if (date != null) {
            url += "?date=" + date.format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        }

        String xmlResponse = httpClient.get(url);

        if (xmlResponse == null || xmlResponse.isEmpty()) {
            throw new RuntimeException("Bank.lv returned empty response");
        }

        Map<String, Double> rates = xmlParser.parse(xmlResponse);

        return rates;
    }
}
