package com.i2i.ems.helper;


public class ForbiddenException extends Exception {
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(Throwable cause) {
        super(cause);
    }
}
