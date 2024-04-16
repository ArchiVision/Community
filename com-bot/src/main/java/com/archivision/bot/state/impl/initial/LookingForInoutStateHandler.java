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
import com.archivision.common.model.entity.Gender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Set;

@Component
@Slf4j
public class LookingForInoutStateHandler extends AbstractStateHandler  {
    public LookingForInoutStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                       KeyboardBuilderService keyboardBuilder, ActiveRegistrationProcessCache registrationProcessCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, registrationProcessCache);
    }

    private static final Set<String> options = Set.of("Хлопців", "Дівчат", "Все одно");

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        UserDto user = registrationProcessCache.getCurrentUser(chatId);
        String messageText = message.getText();
        user.setUserFlowState(UserFlowState.CITY);
        user.setLookingFor(Gender.fromString(messageText));
        messageSender.sendTextMessage(chatId, ResponseTemplate.CITY_INPUT);
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Smth go wrong={}", message.getText());
    }

    @Override
    public boolean isInputValid(Message message) {
        return options.contains(message.getText());
    }

    @Override
    public UserFlowState getState() {
        return UserFlowState.LOOKING;
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }
}
