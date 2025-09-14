package com.example.ai_mvc.common;

public class OnlineException extends Exception {
    private static final long serialVersionUID = 1L;

    public OnlineException(String message) {
        super(message);
    }

    public OnlineException(String message, Throwable cause) {
        super(message, cause);
    }
}