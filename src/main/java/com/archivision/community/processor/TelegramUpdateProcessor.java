package com.archivision.community.processor;

import com.archivision.community.handler.CallbackTelegramEventHandler;
import com.archivision.community.handler.MessageTelegramEventHandler;
import com.archivision.community.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramUpdateProcessor implements UpdateProcessor {
    private final CallbackTelegramEventHandler callbackHandler;
    private final MessageTelegramEventHandler messageHandlers;
    private final UserService userService;

    public void processUpdate(Update update) {
        checkForSpecialUpdates(update);
        if (update.hasMessage()) {
            final Message message = update.getMessage();
            final User user = message.getFrom();
            log.info("Update: updateId: {}, userId: {}, username: {}, text: {}",
                    update.getUpdateId(),
                    user.getId(),
                    user.getUserName(),
                    message.getText()
            );

            messageHandlers.handle(message);
        }
        if (update.hasCallbackQuery()) {
            callbackHandler.handle(update.getCallbackQuery());
        }
    }

    private void checkForSpecialUpdates(Update update) {
        if (update.hasMyChatMember()) {
            final ChatMemberUpdated chatMember = update.getMyChatMember();
            if (chatMember.getNewChatMember().getStatus().equals("kicked")) {
                userService.deleteByTgId(chatMember.getFrom().getId());
            }
        }
    }
}

