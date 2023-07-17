package com.archivision.community.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackHandler implements Handler<CallbackQuery> {
    public void handle(CallbackQuery callbackQuery) {
        log.info("callback");
    }
}
