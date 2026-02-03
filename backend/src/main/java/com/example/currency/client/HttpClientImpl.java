package com.example.currency.client;

import com.example.currency.client.interfaces.HttpClient;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Primary
public class HttpClientImpl implements HttpClient {

    @Override
    public String get(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to make HTTP request", e);
        }
    }
}
