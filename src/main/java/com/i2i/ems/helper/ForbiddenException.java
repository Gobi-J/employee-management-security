package com.i2i.ems.helper;

// For handling forbidden exception
public class ForbiddenException extends Exception {

    public ForbiddenException(String message) {
        super(message);
    }
}
