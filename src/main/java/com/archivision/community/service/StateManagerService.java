package com.archivision.community.service;

import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.document.User;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.util.InputValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class StateManagerService {

    private final InputValidator inputValidator;
    private final UserService userService;
    private final MessageSender messageSender;

    public void manageOtherStates(User user, Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        switch (user.getState()) {
            case NAME -> handleInputNameState(user, chatId, messageText);
            case CITY -> handleInputCityState(user, chatId, messageText);
        }
    }

    private void handleInputCityState(User user, Long chatId, String messageText) {
        if (inputValidator.isCityValid(messageText)) {
            user.setState(User.State.TOPIC);
            user.setCity(messageText);
            userService.addUser(user);
//            messageSender.sendTextMessage(chatId, ResponseTemplate.TOPICS_INPUT);
            SendMessage sendMessage = SendMessage.builder()
                    .text("text")
                    .chatId(chatId)
                    .build();
            sendMessage.setReplyMarkup(ReplyKeyboardMarkup.builder()
                            .keyboardRow(new KeyboardRow(){{
                                add(KeyboardButton.builder()
                                        .text("Пропустити")
                                        .build());
                            }})
                    .build());
            messageSender.sendMessage(sendMessage);
        } else {
            log.error("Місто={} не знайдено", messageText);
            messageSender.sendTextMessage(chatId, "Такого міста не існує або ми " +
                    "забули додати його.");
        }
    }

    private void handleInputNameState(User user, Long chatId, String messageText) {
        if (inputValidator.isNameValid(messageText)) {
            user.setState(User.State.CITY);
            user.setName(messageText);
            userService.addUser(user);
            messageSender.sendTextMessage(chatId, ResponseTemplate.CITY_INPUT);
        } else {
            messageSender.sendTextMessage(chatId, "Щось не так з ім'ям. Спробуй ще раз");
        }
    }
}
