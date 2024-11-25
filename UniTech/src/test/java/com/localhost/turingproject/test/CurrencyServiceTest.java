package com.localhost.turingproject.test;

import static org.junit.jupiter.api.Assertions.*;

import com.localhost.turingproject.config.CurrencyRateCache;
import com.localhost.turingproject.entity.CurrencyRate;
import com.localhost.turingproject.exception.CurrencyNotFoundException;
import com.localhost.turingproject.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class CurrencyServiceTest {

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyRateCache currencyRateCache;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCurrencyRate_Cached() {
        String currencyPair = "USD/AZN";
        CurrencyRate cachedRate = new CurrencyRate(currencyPair, 1.7, LocalDateTime.now());
        when(currencyRateCache.get(currencyPair)).thenReturn(cachedRate);

        Double rate = currencyService.getCurrencyRate(currencyPair);
        assertEquals(1.7, rate);
        verify(currencyRateCache, never()).put(anyString(), any(CurrencyRate.class));
    }

    @Test
    public void testGetCurrencyRate_FetchFromThirdParty() {
        String currencyPair = "USD/AZN";
        when(currencyRateCache.get(currencyPair)).thenReturn(null);

        Double rate = currencyService.getCurrencyRate(currencyPair);
        assertEquals(1.7, rate);
        verify(currencyRateCache).put(eq(currencyPair), any(CurrencyRate.class));
    }

    @Test
    public void testGetCurrencyRate_CurrencyNotFoundException() {
        String currencyPair = "EUR/USD";
        Exception exception = assertThrows(CurrencyNotFoundException.class, () -> {
            currencyService.getCurrencyRate(currencyPair);
        });
        assertEquals("Unsupported currency pair: EUR/USD", exception.getMessage());
    }
}

