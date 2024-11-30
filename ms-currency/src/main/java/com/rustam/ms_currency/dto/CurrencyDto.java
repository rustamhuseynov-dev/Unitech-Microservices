package com.rustam.ms_currency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyDto {

    private String fromCurrency;
    private String toCurrency;
    private double rate;
}
