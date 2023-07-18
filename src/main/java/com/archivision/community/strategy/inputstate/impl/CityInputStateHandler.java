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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.archivision.community.bot.State.TOPIC;

@Component
@Slf4j
public class CityInputStateHandler extends AbstractStateHandler {
    public CityInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                 KeyboardBuilderService keyboardBuilderService) {
        super(inputValidator, userService, messageSender, keyboardBuilderService);
    }

    private static final String ERROR_CITY = "Такого міста не існує або ми ще його не додали. Сорі :(";

    @Override
    public void handle(User user, Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        if (inputValidator.isCityValid(messageText)) {
            user.setState(TOPIC);
            user.setCity(messageText);
            userService.addUser(user);
            sendResponseWithMarkup(chatId, ResponseTemplate.TOPICS_INPUT, keyboardBuilder.generateSkipButton());
        } else {
            log.error("Місто={} не знайдено", messageText);
            messageSender.sendTextMessage(chatId, ERROR_CITY);
        }
    }

    // TODO: 19.07.2023 move it to some place or create common utils package/module
    private void changeStateIfSkippedToWithMessage(Long chatId, String messageText, State state, String userResponseText,
                                                   ReplyKeyboardMarkup markup) {
        if ("Пропустити".equals(messageText)) {
            userService.changeState(chatId, state);
            sendResponseWithMarkup(chatId, userResponseText, markup);
        }
    }

    private void sendResponseWithMarkup(Long chatId, String userResponseText, ReplyKeyboardMarkup markup) {
        SendMessage sendMessage = SendMessage.builder()
                .text(userResponseText)
                .chatId(chatId)
                .replyMarkup(markup)
                .build();
        messageSender.sendMessage(sendMessage);
    }

    @Override
    public State getStateType() {
        return State.CITY;
    }
}