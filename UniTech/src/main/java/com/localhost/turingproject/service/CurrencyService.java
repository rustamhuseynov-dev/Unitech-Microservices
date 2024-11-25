package com.localhost.turingproject.service;

import com.localhost.turingproject.config.CurrencyRateCache;
import com.localhost.turingproject.entity.CurrencyRate;
import com.localhost.turingproject.exception.CurrencyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRateCache cache;

    // Simulated call to third-party API
    private Double fetchRateFromThirdParty(String currencyPair) {
        switch (currencyPair) {
            case "USD/AZN": return 1.7;
            case "AZN/TL": return 8.0;
            default: throw new CurrencyNotFoundException("Unsupported currency pair: " + currencyPair);
        }
    }

    public Double getCurrencyRate(String currencyPair) {
        CurrencyRate cachedRate = cache.get(currencyPair);


        if (cachedRate != null && cachedRate.isRecent()) {
            return cachedRate.getRate();
        }

        Double newRate = fetchRateFromThirdParty(currencyPair);
        cache.put(currencyPair, new CurrencyRate(currencyPair, newRate, LocalDateTime.now()));
        return newRate;
    }
}
