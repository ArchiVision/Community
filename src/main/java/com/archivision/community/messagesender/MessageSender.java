package com.archivision.community.messagesender;

import com.archivision.community.state.OptionalState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface MessageSender {
    void sendTextMessage(Long userId, String message);
    void sendMessage(SendMessage message);
    void sendMsgWithMarkup(Long chatId, String userResponseText, ReplyKeyboardMarkup markup);
    void sendNextStateData(Long chatId, OptionalState.NextStateData nextState);
    void sendMsgWithInline(Long chatId, InlineKeyboardMarkup markup);
}
