package com.example.currency.service;

import com.example.currency.model.ConversionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConversionServiceTest {

    private ConversionService conversionService;

    @BeforeEach
    void setUp() {
        conversionService = new ConversionService();
    }

    @Test
    void testConvert_NormalConversion() {
        Map<String, Double> rates = Map.of("EUR", 1.0, "USD", 1.1);

        ConversionResult result = conversionService.convert(
                100, "EUR", "USD", LocalDate.of(2026, 2, 2), rates
        );

        assertEquals(110.0, result.getConvertedAmount(), 0.0001);
        assertEquals("EUR", result.getFromCurrency());
        assertEquals("USD", result.getToCurrency());
        assertEquals(LocalDate.of(2026, 2, 2), result.getRateDate());
    }


    @Test
    void testConvert_SameCurrency() {
        Map<String, Double> rates = Map.of("EUR", 1.0);

        ConversionResult result = conversionService.convert(
                50, "EUR", "EUR", LocalDate.now(), rates
        );

        assertEquals(50, result.getConvertedAmount());
    }

    @Test
    void testConvert_MissingFromCurrency() {
        Map<String, Double> rates = Map.of("USD", 1.1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            conversionService.convert(100, "EUR", "USD", LocalDate.now(), rates);
        });

        assertTrue(exception.getMessage().contains("Currency not available for the given date"));
    }

    @Test
    void testConvert_MissingToCurrency() {
        Map<String, Double> rates = Map.of("EUR", 1.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            conversionService.convert(100, "EUR", "USD", LocalDate.now(), rates);
        });

        assertTrue(exception.getMessage().contains("Currency not available for the given date"));
    }

    @Test
    void testConvert_ZeroAmount() {
        Map<String, Double> rates = Map.of("EUR", 1.0, "USD", 1.1);

        ConversionResult result = conversionService.convert(0, "EUR", "USD", LocalDate.now(), rates);

        assertEquals(0, result.getConvertedAmount());
    }
}
