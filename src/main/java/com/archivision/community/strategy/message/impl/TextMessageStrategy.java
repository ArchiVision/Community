package com.archivision.community.strategy.message.impl;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.cache.ActiveRegistrationProcessCache;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.command.UserCommands;
import com.archivision.community.dto.UserDto;
import com.archivision.community.entity.User;
import com.archivision.community.mapper.UserMapper;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.model.FilterResult;
import com.archivision.community.service.StateManagerService;
import com.archivision.community.service.ServiceCommandChecker;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserService;
import com.archivision.community.strategy.message.MessageStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TextMessageStrategy implements MessageStrategy {
    private final UserService userService;
    private final MessageSender messageSender;
    private final StateManagerService stateManagerService;
    private final UserCache userCache;
    private final ServiceCommandChecker filterService;

    @Override
    public void handleMessage(Message message) {
        log.info("public void handleMessage(Message message) , TextMessageStrategy");
        FilterResult filterResult = filterService.filter(message);
        if (!filterResult.isProcessNext()) {
            return;
        }

        Long chatId = message.getChatId();
        Optional<User> optionalDbUser = userService.getUserByTelegramId(chatId);
        if (optionalDbUser.isPresent()) {
            User dbUser = optionalDbUser.get();
            stateManagerService.manageOtherStates(dbUser.getUserFlowState(), message);
            return;
        }

        UserDto userDto = userCache.get(chatId);
        if (userDto == null) {
            registerIfNeeded(chatId, message);
        } else {
            stateManagerService.manageOtherStates(userDto.getUserFlowState(), message);
        }
    }

    private void registerIfNeeded(Long chatId, Message message) {
        if (message.getText().contains(UserCommands.START.value())) {
            log.info("User with telegram id={} not found id DB. Saving.", chatId);
            saveUser(chatId, message.getFrom().getUserName());
            messageSender.sendTextMessage(chatId, ResponseTemplate.START);
            messageSender.sendTextMessage(chatId, ResponseTemplate.NAME_INPUT);
        }
    }

    private void saveUser(Long chatId, String username) {
        UserDto userDto = new UserDto();
        userDto.setUserFlowState(UserFlowState.NAME);
        userDto.setTelegramUserId(chatId);
        userDto.setUsername(username);
        userCache.add(chatId, userDto);
    }

    @Override
    public boolean supports(Message message) {
        return message.hasText();
    }
}
