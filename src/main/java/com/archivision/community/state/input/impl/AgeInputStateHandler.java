package com.archivision.community.state.input.impl;

import com.archivision.community.bot.State;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.entity.User;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.community.bot.State.GENDER;

@Component
@Slf4j
public class AgeInputStateHandler extends AbstractStateHandler {
    public AgeInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                KeyboardBuilderService keyboardBuilder) {
        super(inputValidator, userService, messageSender, keyboardBuilder);
    }

    private static final String ERROR_AGE = "Вкажи нормальний вік";

    @Override
    public void doHandle(Message message) {
        User user = userService.getUserByTgId(message.getChatId());
        user.setAge(Long.valueOf(message.getText()));
        user.setState(GENDER);
        userService.updateUser(user);
        messageSender.sendMsgWithMarkup(message.getChatId(), ResponseTemplate.GENDER_INPUT,
                keyboardBuilder.generateGenderButtons());
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Вік={} задано не правильно", message.getText());
        messageSender.sendTextMessage(message.getChatId(), ERROR_AGE);
    }

    @Override
    public boolean valid(Message message) {
        return inputValidator.isAgeValid(message.getText());
    }

    @Override
    public State getStateType() {
        return State.AGE;
    }

    @Override
    public boolean isValidatable() {
        return true;
    }
}
