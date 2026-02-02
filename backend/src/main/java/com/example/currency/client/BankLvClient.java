package com.example.currency.client;

import com.example.currency.util.XmlParser;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

@Component
public class BankLvClient {

    private static final String BASE_URL = "https://www.bank.lv/vk/ecb.xml";
    private final RestTemplate restTemplate;
    private final XmlParser xmlParser;

    public BankLvClient(XmlParser xmlParser) {
        this.restTemplate = new RestTemplate();
        this.xmlParser = xmlParser;
    }

    public Map<String, Double> fetchRates(LocalDate date) {

        try {
            String url = BASE_URL;
            if (date != null) {
                url += "?date=" + date.format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
            }

            String xmlResponse = restTemplate.getForObject(url, String.class);

            if (xmlResponse == null || xmlResponse.isEmpty()) {
                throw new RuntimeException("Bank.lv returned empty response");
            }

            Map<String, Double> onlineRates = xmlParser.parse(xmlResponse);

            return onlineRates;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch rates from Bank.lv: " + e.getMessage(), e);
        }
    }
}
