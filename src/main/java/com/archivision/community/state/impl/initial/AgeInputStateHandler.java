package com.archivision.community.state.impl.initial;

import com.archivision.community.bot.State;
import com.archivision.community.cache.ActiveRegistrationProcessCache;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.dto.UserDto;
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
                                KeyboardBuilderService keyboardBuilder, ActiveRegistrationProcessCache registrationProcessCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, registrationProcessCache);
    }

    private static final String ERROR_AGE = "Вкажи нормальний вік";

    @Override
    public void doHandle(Message message) {
        UserDto user = registrationProcessCache.getCurrentUser(message.getChatId());
        user.setAge(Long.valueOf(message.getText()));
        user.setState(GENDER);
        messageSender.sendMsgWithMarkup(message.getChatId(), ResponseTemplate.GENDER_INPUT,
                keyboardBuilder.genderButtons());
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Вік={} задано не правильно", message.getText());
        messageSender.sendTextMessage(message.getChatId(), ERROR_AGE);
    }

    @Override
    public boolean isInputValid(Message message) {
        return inputValidator.isAgeValid(message.getText());
    }

    @Override
    public State getState() {
        return State.AGE;
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }
}
