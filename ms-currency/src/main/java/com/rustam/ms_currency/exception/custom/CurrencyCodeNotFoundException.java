package com.rustam.ms_currency.exception.custom;

import lombok.Getter;

@Getter
public class CurrencyCodeNotFoundException extends RuntimeException {

    private final String message;

    public CurrencyCodeNotFoundException(String message){
        super(message);
        this.message = message;
    }
}
