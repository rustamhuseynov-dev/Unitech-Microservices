package com.rustam.ms_currency.dto.exception;

import org.springframework.http.HttpStatus;

public interface ResponseMessages {
    String key();
    String message();
    HttpStatus status();
}
