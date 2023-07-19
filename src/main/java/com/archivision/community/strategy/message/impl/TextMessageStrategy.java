package com.archivision.community.strategy.message.impl;

import com.archivision.community.bot.State;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.command.UserCommands;
import com.archivision.community.document.User;
import com.archivision.community.messagesender.MessageSender;
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
public class TextMessageStrategy implements MessageStrategy {
    private final UserService userService;
    private final MessageSender messageSender;
    private final StateManagerService stateManagerService;

    @Override
    public void handleMessage(Message message) {
        log.info("public void handleMessage(Message message) , TextMessageStrategy");
        Long chatId = message.getChatId();
        User user = userService.getUserByTelegramId(chatId).orElse(null);
        if (user != null) {
            stateManagerService.manageOtherStates(user.getState(), message);
        } else if (message.getText().contains(UserCommands.START.value())) {
            registerUser(chatId);
        }
    }

    private void registerUser(Long chatId) {
        log.info("User with telegram id={} not found id DB. Saving.", chatId);
        saveUser(chatId);
        messageSender.sendTextMessage(chatId, ResponseTemplate.START);
        messageSender.sendTextMessage(chatId, ResponseTemplate.NAME_INPUT);
    }

    private void saveUser(Long chatId) {
        User user = new User();
        user.setState(State.NAME);
        user.setTelegramUserId(chatId);
        userService.createUser(user);
    }

    @Override
    public boolean supports(Message message) {
        return message.hasText();
    }
}
