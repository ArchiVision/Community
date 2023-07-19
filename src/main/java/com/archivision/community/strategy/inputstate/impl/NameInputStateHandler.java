package com.archivision.community.strategy.inputstate.impl;

import com.archivision.community.bot.State;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.document.User;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.strategy.inputstate.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.community.bot.State.CITY;

@Component
@Slf4j
public class NameInputStateHandler extends AbstractStateHandler {
    private static final String ERROR_NAME = "Щось не так з ім'ям. Спробуй ще раз";

    public NameInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender, KeyboardBuilderService keyboardBuilder) {
        super(inputValidator, userService, messageSender, keyboardBuilder);
    }

    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        if (inputValidator.isNameValid(messageText)) {
            User user = userService.getUserByTgId(chatId);
            user.setState(CITY);
            user.setName(messageText);
            userService.updateUser(user);
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
