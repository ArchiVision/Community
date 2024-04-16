package com.archivision.bot.exception;

public class MissingTelegramIdException extends RuntimeException {
    public MissingTelegramIdException(String message) {
        super(message);
    }
}
