package com.archivision.community.messagesender;


import com.archivision.community.bot.CommunityBot;
import com.archivision.community.exception.UnableSendMessageException;
import com.archivision.community.state.OptionalState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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

    public void sendMsgWithMarkup(Long chatId, String userResponseText, ReplyKeyboardMarkup markup) {
        SendMessage message = SendMessage.builder()
                .text(userResponseText)
                .chatId(chatId)
                .replyMarkup(markup)
                .build();
        sendMessage(message);
    }

    @Override
    public void sendNextStateData(Long chatId, OptionalState.NextStateData nextState) {
        sendMsgWithMarkup(chatId, nextState.responseText(), nextState.markup());
    }

    @Override
    public void sendMsgWithInline(Long chatId, InlineKeyboardMarkup markup) {
        SendMessage msg = SendMessage.builder()
                .replyMarkup(markup)
                .chatId(chatId)
                .text("Посилання на оплату")
                .build();
        sendMessage(msg);
    }

    @Autowired
    public void setCommunityBot(CommunityBot communityBot) {
        this.communityBot = communityBot;
    }
}
