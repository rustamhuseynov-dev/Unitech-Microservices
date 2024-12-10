package com.rustam.ms_account.dto.exception;

import org.springframework.http.HttpStatus;

public record ExceptionResponseMessages
        (String key,
         String message,
         HttpStatus status) implements ResponseMessages {

}
