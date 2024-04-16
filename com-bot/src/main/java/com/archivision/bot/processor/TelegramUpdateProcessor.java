package com.archivision.bot.processor;

import com.archivision.bot.handler.CallbackTelegramEventHandler;
import com.archivision.bot.handler.MessageTelegramEventHandler;
import com.archivision.bot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramUpdateProcessor implements UpdateProcessor {
    private final CallbackTelegramEventHandler callbackHandler;
    private final MessageTelegramEventHandler messageHandlers;
    private final UserService userService;

    public void processUpdate(Update update) {
        log.info(String.valueOf(update));
        checkForSpecialUpdates(update);
        if (update.hasMessage()) {
            messageHandlers.handle(update.getMessage());
        }
        if (update.hasCallbackQuery()) {
            callbackHandler.handle(update.getCallbackQuery());
        }
    }

    private void checkForSpecialUpdates(Update update) {
        if (update.hasMyChatMember()) {
            ChatMemberUpdated chatMember = update.getMyChatMember();
            if (chatMember.getNewChatMember().getStatus().equals("kicked")) {
                userService.deleteByTgId(chatMember.getFrom().getId());
            }
        }
    }
}

