package com.archivision.community.handler;

import com.archivision.community.service.TelegramImageS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageHandler implements Handler<Message> {

    private final TelegramImageS3Service telegramImageS3Service;

    public void handle(Message message) {
        log.info("Text message");

        if (message.hasPhoto()) {
            String fileId = message.getPhoto().get(2).getFileId();
            Long chatId = message.getChatId();
            telegramImageS3Service.uploadImage(String.valueOf(chatId), fileId);
        }
    }
}
