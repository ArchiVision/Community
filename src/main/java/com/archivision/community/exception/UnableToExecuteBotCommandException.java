package com.archivision.community.exception;

public class UnableToExecuteBotCommandException extends RuntimeException {
    public UnableToExecuteBotCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
