package com.archivision.community.processor;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProcessor {
    void processUpdate(Update update);
}