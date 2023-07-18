package com.archivision.community.messagesender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {
    void sendTextMessage(Long userId, String message);
    void sendMessage(SendMessage message);
}
