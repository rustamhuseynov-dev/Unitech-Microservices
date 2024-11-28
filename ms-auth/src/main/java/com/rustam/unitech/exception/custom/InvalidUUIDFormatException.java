package com.rustam.unitech.exception.custom;

public class InvalidUUIDFormatException extends RuntimeException {
    public InvalidUUIDFormatException(String s, IllegalArgumentException e) {
        super(s,e);
    }
}
