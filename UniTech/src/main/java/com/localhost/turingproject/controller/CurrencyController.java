package com.localhost.turingproject.controller;

import com.localhost.turingproject.exception.CurrencyNotFoundException;
import com.localhost.turingproject.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/rate/{currencyPair}")
    public ResponseEntity<?> getCurrencyRate(@PathVariable String currencyPair) {
        try {
            Double rate = currencyService.getCurrencyRate(currencyPair);
            return ResponseEntity.ok(rate);
        } catch (CurrencyNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
}
