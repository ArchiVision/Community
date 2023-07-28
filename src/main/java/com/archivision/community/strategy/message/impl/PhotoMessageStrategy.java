package com.archivision.community.strategy.message.impl;

import com.archivision.community.bot.State;
import com.archivision.community.entity.User;
import com.archivision.community.service.StateManagerService;
import com.archivision.community.service.UserService;
import com.archivision.community.strategy.message.MessageStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
@RequiredArgsConstructor
public class PhotoMessageStrategy implements MessageStrategy {
    private final StateManagerService stateManagerService;
    private final UserService userService;
    @Override
    public void handleMessage(Message message) {
        Long chatId = message.getChatId();
        userService.getUserByTelegramId(chatId)
                .ifPresentOrElse(user -> {
                    if (ifNotInPhotoState(user)) return;
                    stateManagerService.manageOtherStates(user.getState(), message);
                }, () -> log.info("User with tg id={} is not registered in DB", chatId));
    }

    private static boolean ifNotInPhotoState(User user) {
        if (!user.getState().equals(State.PHOTO)) {
            log.info("User={} in state={}. Do not process picture", user.getTelegramUserId(), user.getState());
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(Message message) {
        return message.hasPhoto();
    }
}
