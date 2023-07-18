package com.archivision.community.strategy.inputstate.impl;

import com.archivision.community.bot.State;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.document.User;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.UserService;
import com.archivision.community.strategy.inputstate.StateHandler;
import com.archivision.community.util.InputValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.community.bot.State.CITY;

@Component
@RequiredArgsConstructor
@Slf4j
public class NameInputStateHandler implements StateHandler {
    private static final String ERROR_NAME = "Щось не так з ім'ям. Спробуй ще раз";
    private final InputValidator inputValidator;
    private final UserService userService;
    private final MessageSender messageSender;

    @Override
    public void handle(User user, Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        if (inputValidator.isNameValid(messageText)) {
            user.setState(CITY);
            user.setName(messageText);
            userService.addUser(user);
            messageSender.sendTextMessage(chatId, ResponseTemplate.CITY_INPUT);
        } else {
            messageSender.sendTextMessage(chatId, ERROR_NAME);
        }
    }

    @Override
    public State getStateType() {
        return State.NAME;
    }
}
