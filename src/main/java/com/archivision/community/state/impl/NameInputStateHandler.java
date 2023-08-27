package com.archivision.community.state.impl;

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

import static com.archivision.community.bot.State.AGE;

@Component
@Slf4j
public class NameInputStateHandler extends AbstractStateHandler {
    private static final String ERROR_NAME = "Щось не так з ім'ям. Спробуй ще раз";

    public NameInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender, KeyboardBuilderService keyboardBuilder) {
        super(inputValidator, userService, messageSender, keyboardBuilder);
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        User user = userService.getUserByTgId(chatId);
        user.setState(AGE);
        user.setName(messageText);
        userService.updateUser(user);
        messageSender.sendTextMessage(chatId, ResponseTemplate.AGE_INPUT);
    }

    @Override
    public void onValidationError(Message message) {
        messageSender.sendTextMessage(message.getChatId(), ERROR_NAME);
    }

    @Override
    public boolean valid(Message message) {
        return inputValidator.isNameValid(message.getText());
    }

    @Override
    public State getStateType() {
        return State.NAME;
    }

    @Override
    public boolean isValidatable() {
        return true;
    }
}
