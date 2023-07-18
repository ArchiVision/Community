package com.archivision.community.handler;

import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.TelegramImageS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackHandler implements Handler<CallbackQuery> {
    private final MessageSender messageSender;
    private final TelegramImageS3Service telegramImageS3Service;
    public void handle(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().contains("hello_btn")) {
            messageSender.sendMessage(SendMessage.builder()
                            .text("Your picture")
                    .chatId(callbackQuery.getMessage().getChatId())
                    .build());
            telegramImageS3Service.sendImageToUser(callbackQuery.getMessage().getChatId());
        }
        log.info("callback");
    }
}
