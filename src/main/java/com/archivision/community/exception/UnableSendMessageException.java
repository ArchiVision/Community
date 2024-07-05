package com.archivision.community.exception;

public class UnableSendMessageException extends UnableToExecuteBotCommandException {
    public UnableSendMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
