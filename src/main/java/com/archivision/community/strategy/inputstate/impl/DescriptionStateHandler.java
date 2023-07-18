package com.archivision.community.strategy.inputstate.impl;

import com.archivision.community.bot.State;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.document.User;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.strategy.inputstate.StateHandler;
import com.archivision.community.util.InputValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.archivision.community.bot.State.APPROVE;

@Component
@Slf4j
@RequiredArgsConstructor
public class DescriptionStateHandler implements StateHandler {
    private final InputValidator inputValidator;
    private final UserService userService;
    private final MessageSender messageSender;
    private final KeyboardBuilderService keyboardBuilder;

    @Override
    public void handle(User user, Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        changeStateIfSkippedToWithMessage(chatId, messageText, APPROVE, ResponseTemplate.APPROVE_INPUT, keyboardBuilder.generateApprovalButtons());
        if (inputValidator.isDescriptionValid(messageText)) {

        } else {
            // TODO: 19.07.2023 change this message to smth diff
            log.error("?? desc");
        }
    }

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
        return State.DESCRIPTION;
    }
}
