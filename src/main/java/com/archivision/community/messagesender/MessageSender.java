package com.archivision.community.messagesender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface MessageSender {
    void sendTextMessage(Long userId, String message);
    void sendMessage(SendMessage message);
    void sendMsgWithMarkup(Long chatId, String userResponseText, ReplyKeyboardMarkup markup);
}
