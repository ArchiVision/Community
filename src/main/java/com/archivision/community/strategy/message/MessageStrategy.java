package com.archivision.community.strategy.message;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageStrategy {
    boolean supports(Message message);
    void handleMessage(Message message);
}
