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

import static com.archivision.common.model.bot.UserFlowState.TOPIC;

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
        user.setUserFlowState(TOPIC);
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
    public UserFlowState getState() {
        return UserFlowState.CITY;
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }
}