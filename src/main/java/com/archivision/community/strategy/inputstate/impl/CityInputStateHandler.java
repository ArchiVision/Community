package com.archivision.community.strategy.inputstate.impl;

import com.archivision.community.bot.State;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.document.User;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.strategy.inputstate.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.community.bot.State.AGE;

@Component
@Slf4j
public class CityInputStateHandler extends AbstractStateHandler {
    public CityInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                 KeyboardBuilderService keyboardBuilderService) {
        super(inputValidator, userService, messageSender, keyboardBuilderService);
    }

    private static final String ERROR_CITY = "Такого міста не існує або ми ще його не додали. Сорі :(";

    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        if (inputValidator.isCityValid(messageText)) {
            User user = userService.getUserByTgId(chatId);
            user.setState(AGE);
            user.setCity(messageText);
            userService.updateUser(user);
            messageSender.sendTextMessage(chatId, ResponseTemplate.AGE_INPUT);
        } else {
            log.error("Місто={} не знайдено", messageText);
            messageSender.sendTextMessage(chatId, ERROR_CITY);
        }
    }

    @Override
    public State getStateType() {
        return State.CITY;
    }
}