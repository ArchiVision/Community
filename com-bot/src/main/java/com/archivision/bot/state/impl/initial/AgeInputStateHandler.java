package com.archivision.bot.state.impl.initial;


import com.archivision.bot.cache.ActiveRegistrationProcessCache;
import com.archivision.bot.command.ResponseTemplate;
import com.archivision.bot.sender.MessageSender;
import com.archivision.bot.service.KeyboardBuilderService;
import com.archivision.bot.service.user.UserService;
import com.archivision.bot.state.AbstractStateHandler;
import com.archivision.bot.util.InputValidator;
import com.archivision.common.model.bot.UserFlowState;
import com.archivision.common.model.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.common.model.bot.UserFlowState.GENDER;


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
        user.setUserFlowState(GENDER);
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
    public UserFlowState getState() {
        return UserFlowState.AGE;
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }
}
