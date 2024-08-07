package com.archivision.community.state.impl.initial;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.dto.UserDto;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.community.bot.UserFlowState.TOPIC;

@Component
@Slf4j
public class CityInputStateHandler extends AbstractStateHandler {
    public CityInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                 KeyboardBuilderService keyboardBuilderService, UserCache userCache) {
        super(inputValidator, userService, messageSender, keyboardBuilderService, userCache);
    }

    private static final String ERROR_CITY = "Такого міста не існує або ми ще його не додали. Сорі :(";

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        UserDto user = userCache.get(chatId);
        String messageText = message.getText();
        userCache.processUser(chatId, userDto -> {
           userDto.setCity(messageText);
        });
       userService.changeState(chatId, TOPIC);
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

    @Override
    public void onStateChanged(Long chatId) {
        messageSender.sendTextMessage(chatId, ResponseTemplate.CITY_INPUT);
    }
}