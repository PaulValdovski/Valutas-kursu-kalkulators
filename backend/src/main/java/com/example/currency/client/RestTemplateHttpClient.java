package com.example.currency.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.currency.client.interfaces.HttpClient;

@Component
public class RestTemplateHttpClient implements HttpClient {

    private final RestTemplate restTemplate;

    public RestTemplateHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String get(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}
