package com.archivision.community.exception;

public class MissingTelegramIdException extends RuntimeException {
    public MissingTelegramIdException(String message) {
        super(message);
    }
}
