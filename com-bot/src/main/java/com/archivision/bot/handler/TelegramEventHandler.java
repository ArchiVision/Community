package com.archivision.bot.handler;

public interface TelegramEventHandler<T> {
    void handle(T update);
}
