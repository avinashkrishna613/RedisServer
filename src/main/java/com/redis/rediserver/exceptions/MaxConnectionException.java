package com.redis.rediserver.exceptions;

public class MaxConnectionException extends RuntimeException {
    private String message;

    public MaxConnectionException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
