package com.localhost.turingproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class CurrencyRate {

    private final String currencyPair;
    private final Double rate;
    private final LocalDateTime lastUpdated;

    public boolean isRecent() {
        return lastUpdated.isAfter(LocalDateTime.now().minusMinutes(1));
    }
}
