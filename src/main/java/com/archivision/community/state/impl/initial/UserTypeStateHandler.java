package com.archivision.community.state.impl.initial;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.dto.UserDto;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.model.UserType;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.community.bot.UserFlowState.NAME;

@Component
@Slf4j
public class UserTypeStateHandler extends AbstractStateHandler {
    public UserTypeStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                KeyboardBuilderService keyboardBuilder, UserCache userCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, userCache);
    }

    private static final String ERROR_TYPE = "Вкажи тип. Людина/Юніт";

    @Override
    public void doHandle(Message message) {
        UserDto user = userCache.get(message.getChatId());
        user.setUserType(UserType.fromString(message.getText()));
        user.setUserFlowState(NAME);
        userCache.add(message.getChatId(), user);
        messageSender.sendTextMessage(message.getChatId(), ResponseTemplate.NAME_INPUT);
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Тип користувача={} задано не правильно", message.getText());
        messageSender.sendTextMessage(message.getChatId(), ERROR_TYPE);
    }

    @Override
    public boolean isInputValid(Message message) {
        return true;
    }

    @Override
    public UserFlowState getState() {
        return UserFlowState.TYPE;
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }
}
