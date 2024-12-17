package com.rustam.ms_currency.service;

import com.rustam.ms_currency.dto.CurrencyRequest;
import com.rustam.ms_currency.exception.custom.CurrencyCodeNotFoundException;
import com.rustam.ms_currency.model.Currency;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public Map<Object, Object> getAllCurrencies() {
        return redisTemplate.opsForHash().entries("currencies");
    }

    public Object getCurrency(CurrencyRequest currencyRequest) {
        String baseCurrency = currencyRequest.getBaseCurrency().toUpperCase();
        String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_qG9OgrdEqoD2aj2XOnRrZn5kCmI8w5YPFIYaao8Z"
                +"&currencies=EUR,USD,CAD&base_currency=" + baseCurrency;

        String redisKey = "currencies:" + baseCurrency;
        Boolean isCurrenciesExist = redisTemplate.hasKey(redisKey);

        if (Boolean.FALSE.equals(isCurrenciesExist)) {
            Map<String, Object> currencies = restTemplate.getForObject(apiUrl, Map.class);
            if (currencies != null && currencies.containsKey("data")) {
                Map<String, Number> rates = (Map<String, Number>) currencies.get("data");
                rates.forEach((currency, rate) -> redisTemplate.opsForHash().put(redisKey, currency, rate));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Currencies data is null or invalid from API");
            }
        }

        Map<Object, Object> cachedRates = redisTemplate.opsForHash().entries(redisKey);

        String requestedCurrency = currencyRequest.getCurrencyCode().toUpperCase();
        Object redisValue = cachedRates.get(requestedCurrency);

        if (redisValue == null) {
            throw new CurrencyCodeNotFoundException("Currency not found in Redis: " + requestedCurrency);
        }

        // Valyutanın dəyərini yoxla və qaytar
        if (redisValue instanceof Number) {
            return redisValue;
        }

        // Əlavə olaraq kompleks obyektlər üçün yoxlama
        if (redisValue instanceof Currency) {
            Currency currency = (Currency) redisValue;
            if (!Objects.equals(currency.getCurrencyCode(), requestedCurrency)) {
                throw new CurrencyCodeNotFoundException("Currency code mismatch for: " + requestedCurrency);
            }
            return currency.getValue();
        }

        return redisValue;
    }

}