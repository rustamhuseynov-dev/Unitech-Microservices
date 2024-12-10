package com.rustam.ms_account.exception.custom;

import lombok.Getter;

@Getter
public class ResponseNotFoundException extends RuntimeException {
    private final String message;

    public ResponseNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
