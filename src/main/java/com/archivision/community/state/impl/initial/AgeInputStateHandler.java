package com.archivision.community.state.impl.initial;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.community.bot.UserFlowState.GENDER;

@Component
@Slf4j
public class AgeInputStateHandler extends AbstractStateHandler {
    public AgeInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                KeyboardBuilderService keyboardBuilder, UserCache userCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, userCache);
    }

    private static final String ERROR_AGE = "Вкажи нормальний вік";

    @Override
    public void doHandle(Message message) {
        long chatId = message.getChatId();
        userCache.processUser(chatId, userDto -> {
            long age = Long.parseLong(message.getText());
            userDto.setAge(age);
        });
        userService.changeState(chatId, GENDER);
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

    @Override
    public void onStateChanged(Long chatId) {
        messageSender.sendTextMessage(chatId, ResponseTemplate.AGE_INPUT);
    }
}
