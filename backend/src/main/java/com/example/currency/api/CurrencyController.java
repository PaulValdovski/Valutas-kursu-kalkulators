package com.example.currency.api;

import com.example.currency.model.ConversionResult;
import com.example.currency.service.ConversionService;
import com.example.currency.service.RateService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class CurrencyController {

    private final RateService rateService;
    private final ConversionService conversionService;

    public CurrencyController(RateService rateService, ConversionService conversionService) {
        this.rateService = rateService;
        this.conversionService = conversionService;
    }

    @GetMapping("/convert")
    public ResponseEntity<ConversionResult> convert(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        try {
            Map<String, Double> rates = rateService.getRates(date);
            ConversionResult result = conversionService.convert(amount, from, to, date, rates);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ConversionResult(from, to, amount, 0.0, date));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ConversionResult(from, to, amount, 0.0, date));
        }
    }
}
