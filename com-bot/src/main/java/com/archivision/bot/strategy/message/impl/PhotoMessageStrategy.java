package com.archivision.bot.strategy.message.impl;

import com.archivision.bot.cache.ActiveRegistrationProcessCache;
import com.archivision.bot.service.StateManagerService;
import com.archivision.bot.strategy.message.MessageStrategy;
import com.archivision.common.model.bot.UserFlowState;
import com.archivision.common.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
@RequiredArgsConstructor
public class PhotoMessageStrategy implements MessageStrategy {
    private final StateManagerService stateManagerService;
    private final ActiveRegistrationProcessCache registrationProcessCache;
    @Override
    public void handleMessage(Message message) {
        Long chatId = message.getChatId();
        registrationProcessCache.get(chatId).ifPresentOrElse(user -> {
            if (ifNotInPhotoState(user)) return;
            stateManagerService.manageOtherStates(user.getUserFlowState(), message);
        }, () -> log.info("User with tg id={} is not registered in DB", chatId));
    }

    private static boolean ifNotInPhotoState(UserDto user) {
        if (!user.getUserFlowState().equals(UserFlowState.PHOTO)) {
            log.info("User={} in state={}. Do not process picture", user.getTelegramUserId(), user.getUserFlowState());
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(Message message) {
        return message.hasPhoto();
    }
}
