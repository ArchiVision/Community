package com.archivision.community.messagesender;


import com.archivision.community.bot.CommunityBot;
import com.archivision.community.exception.bot.UnableSendMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TgMessageSender implements MessageSender {
    private CommunityBot communityBot;

    @Override
    public void sendTextMessage(Long userId, String message) {
        log.info("Sending text message to={}", userId);
        executeSendMessage(userId, message);
    }

    @Override
    public void sendMessage(SendMessage message) {
        try {
            communityBot.execute(message);
        } catch (TelegramApiException e) {
            throw new UnableSendMessageException("Unable to send a message", e);
        }
    }

    private void executeSendMessage(Long userId, String message) {
        try {
            communityBot.execute(SendMessage.builder()
                    .chatId(userId)
                    .text(message)
                    .build());
        } catch (TelegramApiException e) {
            throw new UnableSendMessageException("Unable to send a message", e);
        }
    }

    @Autowired
    public void setCommunityBot(CommunityBot communityBot) {
        this.communityBot = communityBot;
    }
}
