package com.archivision.community.strategy.inputstate.impl;

import com.archivision.community.bot.State;
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

@Component
@Slf4j
public class TopicsInputStateHandler extends AbstractStateHandler {

    public TopicsInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender, KeyboardBuilderService keyboardBuilderService) {
        super(inputValidator, userService, messageSender, keyboardBuilderService);
    }

    @Override
    public void handle(User user, Message message) {
        messageSender.sendMessage(SendMessage.builder()
                        .chatId(message.getChatId())
                        .text("test")
                .build());

//        changeStateIfSkippedToWithMessage(chatId, messageText, DESCRIPTION, ResponseTemplate.DESC_INPUT, keyboardBuilder.generateSkipButton());
//        if (inputValidator.isTopicValid(messageText)) {
//
//        } else {
//            // TODO: 19.07.2023 change this message to smth diff
//            log.error("Не валідний топік ?? topic");
//        }
    }

    @Override
    public State getStateType() {
        return State.TOPIC;
    }
}
