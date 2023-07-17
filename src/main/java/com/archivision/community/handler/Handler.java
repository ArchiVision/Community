package com.archivision.community.handler;

public interface Handler<T> {
    void handle(T update);
}
