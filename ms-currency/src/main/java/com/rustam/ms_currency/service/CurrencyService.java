package com.rustam.ms_currency.service;

import com.rustam.ms_currency.exception.custom.CurrencyCodeNotFoundException;
import com.rustam.ms_currency.model.Currency;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CurrencyService {

    RedisTemplate<String,Object> redisTemplate;
    RestTemplate restTemplate;
    @Value("${spring.currency.api-key}")
    static String apiKey;


    public void save() {
        String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey="+apiKey+"&currencies=EUR,USD,CAD&base_currency=USD   ";

        Map<String, Object> currencies = restTemplate.getForObject(apiUrl, Map.class);
        Map<String, Double> rates = (Map<String, Double>) currencies.get("data");
        rates.forEach((currency, rate) -> redisTemplate.opsForHash().put("currencies", currency, rate));
    }

    public Map<Object, Object> getAllCurrencies() {
        return redisTemplate.opsForHash().entries("currencies");
    }

    public Object getCurrency(String currencyCode) {
        Object redisValue = redisTemplate.opsForHash().get("currencies", currencyCode);

        if (redisValue == null) {
            throw new CurrencyCodeNotFoundException("Currency not found in Redis");
        }

        if (redisValue instanceof Double) {
            return redisValue;
        }

        if (redisValue instanceof Currency) {
            Currency currencies = (Currency) redisValue;
            if (!Objects.equals(currencies.getCurrencyCode(), currencyCode)) {
                throw new CurrencyCodeNotFoundException("Currency code mismatch");
            }
            return currencies.getValue();
        }
        return redisValue;
    }
}