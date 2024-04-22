package com.archivision.community.strategy.message.impl;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.dto.UserDto;
import com.archivision.community.service.StateManagerService;
import com.archivision.community.service.UserCache;
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
    private final UserCache userCache;
    @Override
    public void handleMessage(Message message) {
        Long chatId = message.getChatId();
        userCache.processUser(chatId, userDto -> {
            if (ifNotInPhotoState(userDto)) return;
            stateManagerService.manageOtherStates(userDto.getUserFlowState(), message);
        });
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
