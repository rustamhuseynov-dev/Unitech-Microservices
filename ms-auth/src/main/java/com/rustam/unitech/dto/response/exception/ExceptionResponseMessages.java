package com.rustam.unitech.dto.response.exception;

import org.springframework.http.HttpStatus;


public record ExceptionResponseMessages
        (String key,
         String message,
         HttpStatus status) implements ResponseMessages {

}
