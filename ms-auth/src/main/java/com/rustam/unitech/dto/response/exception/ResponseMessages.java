package com.rustam.unitech.dto.response.exception;

import org.springframework.http.HttpStatus;

public interface ResponseMessages {

    String key();
    String message();
    HttpStatus status();
}
