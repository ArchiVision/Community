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

import static com.archivision.community.bot.State.TOPIC;

@Component
@Slf4j
public class AgeInputStateHandler extends AbstractStateHandler {
    public AgeInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                KeyboardBuilderService keyboardBuilder) {
        super(inputValidator, userService, messageSender, keyboardBuilder);
    }

    private static final String ERROR_AGE = "Вкажи нормальний вік";

    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        if (inputValidator.isAgeValid(messageText)) {
            User user = userService.getUserByTgId(chatId);
            user.setState(TOPIC);
            user.setAge(Long.valueOf(messageText));
            userService.updateUser(user);
            messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.TOPICS_INPUT, keyboardBuilder.generateSkipButton());
        } else {
            log.error("Вік={} задано не правильно", messageText);
            messageSender.sendTextMessage(chatId, ERROR_AGE);
        }
    }

    @Override
    public State getStateType() {
        return State.AGE;
    }
}
