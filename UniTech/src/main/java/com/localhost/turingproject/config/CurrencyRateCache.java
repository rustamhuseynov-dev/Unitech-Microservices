package com.localhost.turingproject.config;

import com.localhost.turingproject.entity.CurrencyRate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class CurrencyRateCache {

    private final Map<String, CurrencyRate> rateCache = new ConcurrentHashMap<>();

    public CurrencyRate get(String currencyPair) {
        return rateCache.get(currencyPair);
    }

    public void put(String currencyPair, CurrencyRate currencyRate) {
        rateCache.put(currencyPair, currencyRate);
    }

    public void evictExpiredRates() {
        rateCache.values().removeIf(rate -> !rate.isRecent());
    }
}
