package com.archivision.community.processor;

import com.archivision.community.handler.CallbackHandler;
import com.archivision.community.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramUpdateProcessor implements UpdateProcessor {
    private final CallbackHandler callbackHandler;
    private final MessageHandler messageHandlers;

    public void processUpdate(Update update) {
        if (update.hasMessage()) {
            messageHandlers.handle(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            callbackHandler.handle(update.getCallbackQuery());
        }
    }
}

