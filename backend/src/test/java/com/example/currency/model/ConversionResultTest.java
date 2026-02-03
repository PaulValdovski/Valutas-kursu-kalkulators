package com.example.currency.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ConversionResultTest {

    @Test
    void testValidConversionResult() {
        LocalDate date = LocalDate.of(2026, 2, 3);
        ConversionResult result = new ConversionResult("USD", "EUR", 100.0, 92.0, date);

        assertEquals("USD", result.getFromCurrency());
        assertEquals("EUR", result.getToCurrency());
        assertEquals(100.0, result.getOriginalAmount());
        assertEquals(92.0, result.getConvertedAmount());
        assertEquals(date, result.getRateDate());
    }

    @Test
    void testNullFromCurrencyThrowsException() {
        LocalDate date = LocalDate.now();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new ConversionResult(null, "EUR", 100.0, 92.0, date));
        assertEquals("From currency cannot be null", exception.getMessage());
    }

    @Test
    void testEmptyToCurrencyThrowsException() {
        LocalDate date = LocalDate.now();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new ConversionResult("USD", "", 100.0, 92.0, date));
        assertEquals("To currency cannot be null", exception.getMessage());
    }

    @Test
    void testNullRateDateThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new ConversionResult("USD", "EUR", 100.0, 92.0, null));
        assertEquals("Rate date cannot be null", exception.getMessage());
    }

    @Test
    void testToStringContainsValues() {
        LocalDate date = LocalDate.of(2026, 2, 3);
        ConversionResult result = new ConversionResult("USD", "EUR", 100.0, 92.0, date);
        String str = result.toString();
        assertTrue(str.contains("USD"));
        assertTrue(str.contains("EUR"));
        assertTrue(str.contains("100.0"));
        assertTrue(str.contains("92.0"));
        assertTrue(str.contains("2026-02-03"));
    }
}
