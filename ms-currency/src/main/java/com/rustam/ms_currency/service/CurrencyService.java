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


    public void save() {

    }

    public Map<Object, Object> getAllCurrencies() {
        return redisTemplate.opsForHash().entries("currencies");
    }

    public Object getCurrency(CurrencyRequest currencyRequest) {
        // Base currency və API URL-i dinamik olaraq qurulur
        String baseCurrency = currencyRequest.getBaseCurrency().toUpperCase();
        String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_qG9OgrdEqoD2aj2XOnRrZn5kCmI8w5YPFIYaao8Z"
                +"&currencies=EUR,USD,CAD&base_currency=" + baseCurrency;

        String redisKey = "currencies:" + baseCurrency; // Redis üçün unikal açar
        Boolean isCurrenciesExist = redisTemplate.hasKey(redisKey);

        if (Boolean.FALSE.equals(isCurrenciesExist)) {
            // Redis-də yoxdur, API-dən məlumatı gətir
            Map<String, Object> currencies = restTemplate.getForObject(apiUrl, Map.class);
            if (currencies != null && currencies.containsKey("data")) {
                // currencies-in "data" hissəsini al
                Map<String, Number> rates = (Map<String, Number>) currencies.get("data");

                // Redis-ə yaz
                rates.forEach((currency, rate) -> redisTemplate.opsForHash().put(redisKey, currency, rate));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Currencies data is null or invalid from API");
            }
        }

        // Redis-dən məlumatı oxu
        Map<Object, Object> cachedRates = redisTemplate.opsForHash().entries(redisKey);

        // İstifadəçi tərəfindən sorğulanan valyuta kodunu al
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