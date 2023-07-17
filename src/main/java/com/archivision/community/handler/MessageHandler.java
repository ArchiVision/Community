package com.archivision.community.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageHandler implements Handler<Message> {
    public void handle(Message message) {
        log.info("Text message");
    }
}
