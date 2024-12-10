package com.rustam.ms_currency.dto.exception;

import org.springframework.http.HttpStatus;

public record ExceptionResponseMessages
        (String key,
         String message,
         HttpStatus status) implements ResponseMessages {

}
