package com.archivision.community.messagesender;


import com.archivision.community.bot.BroadcasterBot;
import com.archivision.community.exception.bot.UnableSendMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
@RequiredArgsConstructor
public class TgMessageSender implements MessageSender {
    @Lazy
    @Autowired
    private BroadcasterBot broadcasterBot;

    @Override
    public void sendTextMessage(String userId, String message) {
        log.info("Sending text message to={}", userId);
        executeSendMessage(userId, message);
    }

    @Override
    public void sendMessage(SendMessage message) {
        try {
            log.info("Sending message to={}", message.getChatId());
            broadcasterBot.execute(message);
        } catch (TelegramApiException e) {
            throw new UnableSendMessageException("Unable to send a message", e);
        }
    }

    private void executeSendMessage(String userId, String message) {
        try {
            broadcasterBot.execute(SendMessage.builder()
                    .chatId(userId)
                    .text(message)
                    .build());
        } catch (TelegramApiException e) {
            throw new UnableSendMessageException("Unable to send a message", e);
        }
    }
}
