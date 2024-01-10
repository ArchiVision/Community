package com.archivision.community.state.impl;

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

import static com.archivision.community.bot.State.TOPIC;

@Component
@Slf4j
public class CityInputStateHandler extends AbstractStateHandler {
    public CityInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                 KeyboardBuilderService keyboardBuilderService, ActiveRegistrationProcessCache registrationProcessCache) {
        super(inputValidator, userService, messageSender, keyboardBuilderService, registrationProcessCache);
    }

    private static final String ERROR_CITY = "Такого міста не існує або ми ще його не додали. Сорі :(";

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        UserDto user = registrationProcessCache.getCurrentUser(chatId);
        String messageText = message.getText();
        user.setState(TOPIC);
        user.setCity(messageText);
        messageSender.sendMsgWithMarkup(message.getChatId(), ResponseTemplate.TOPICS_INPUT,
                keyboardBuilder.skipButton());
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Місто={} не знайдено", message.getText());
        messageSender.sendTextMessage(message.getChatId(), ERROR_CITY);
    }

    @Override
    public boolean isInputValid(Message message) {
        return inputValidator.isCityValid(message.getText());
    }

    @Override
    public State getState() {
        return State.CITY;
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }
}