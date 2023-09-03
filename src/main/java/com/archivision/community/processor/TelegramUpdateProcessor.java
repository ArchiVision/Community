package com.archivision.community.processor;

import com.archivision.community.handler.CallbackHandler;
import com.archivision.community.handler.MessageHandler;
import com.archivision.community.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramUpdateProcessor implements UpdateProcessor {
    private final CallbackHandler callbackHandler;
    private final MessageHandler messageHandlers;
    private final UserService userService;

    public void processUpdate(Update update) {
        log.info(String.valueOf(update));
        if (update.hasMessage()) {
            messageHandlers.handle(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            callbackHandler.handle(update.getCallbackQuery());
        } else if (update.hasMyChatMember()) {
            ChatMemberUpdated chatMember = update.getMyChatMember();
            if (chatMember.getNewChatMember().getStatus().equals("kicked")) {
                userService.deleteByTgId(chatMember.getFrom().getId());
            }
        }
    }
}

