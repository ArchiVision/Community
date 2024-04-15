package com.archivision.community.handler;

public interface TelegramEventHandler<T> {
    void handle(T update);
}
