package com.archivision.community.handler;

import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.TelegramImageS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageHandler implements Handler<Message> {
    private final TelegramImageS3Service telegramImageS3Service;
    private final MessageSender messageSender;

    public void handle(Message message) {
        log.info("Text message");

        if (message.hasText()) {
            String text = message.getText();
            if (text.contains("/start")) {
                // ---
                SendMessage sendMessage = SendMessage.builder()
                        .text("some text")
                        .chatId(message.getChatId())
                        .build();

                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                keyboard.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder()
                                        .text("Say hello")
                                        .callbackData("hello_btn")
                                        .build()));
                inlineKeyboardMarkup.setKeyboard(keyboard);
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                messageSender.sendMessage(sendMessage);
            }
        }
    }
}
