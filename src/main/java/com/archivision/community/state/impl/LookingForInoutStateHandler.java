package com.archivision.community.state.impl;

import com.archivision.community.bot.State;
import com.archivision.community.cache.ActiveRegistrationProcessCache;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.dto.UserDto;
import com.archivision.community.entity.Gender;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
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
        user.setState(State.CITY);
        user.setLookingFor(Gender.fromString(messageText));
        messageSender.sendTextMessage(chatId, ResponseTemplate.CITY_INPUT);
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Smth go wrong={}", message.getText());
    }

    @Override
    public boolean valid(Message message) {
        return options.contains(message.getText());
    }

    @Override
    public State getStateType() {
        return State.LOOKING;
    }

    @Override
    public boolean isValidatable() {
        return true;
    }
}
