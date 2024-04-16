package com.archivision.bot.state.impl;


import com.archivision.bot.cache.ActiveRegistrationProcessCache;
import com.archivision.bot.model.Reply;
import com.archivision.bot.sender.MessageSender;
import com.archivision.bot.service.KeyboardBuilderService;
import com.archivision.bot.service.user.UserInteractionService;
import com.archivision.bot.service.user.UserService;
import com.archivision.bot.state.AbstractStateHandler;
import com.archivision.bot.state.WithReplyOptions;
import com.archivision.bot.util.InputValidator;
import com.archivision.common.model.bot.UserFlowState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Component
@Slf4j
public class MatchStateHandler extends AbstractStateHandler implements WithReplyOptions {
    private final UserInteractionService userInteractionService;

    public MatchStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                             KeyboardBuilderService keyboardBuilder, ActiveRegistrationProcessCache registrationProcessCache,
                             UserInteractionService userLikeService) {
        super(inputValidator, userService, messageSender, keyboardBuilder, registrationProcessCache);
        this.userInteractionService = userLikeService;
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        if (isLiked(messageText)) {
            userInteractionService.handleLikeAction(chatId);
        }
        if (isDisliked(messageText)) {
            userInteractionService.handleDislikeAction(chatId);
        }
        if (messageText.equals(Reply.SETTINGS.toString())) {
            messageSender.sendMsgWithMarkup(chatId, "Налаштування", keyboardBuilder.subscriptions());
            userService.changeState(chatId, UserFlowState.SETTINGS);
        }
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Smth went wrong. Message={}", message.getText());
    }

    @Override
    public boolean isInputValid(Message message) {
        return getOptions().contains(message.getText());
    }

    @Override
    public UserFlowState getState() {
        return UserFlowState.MATCH;
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }

    private boolean isLiked(String msg) {
        return "+".equals(msg);
    }

    private boolean isDisliked(String msg) {
        return "-".equals(msg);
    }

    @Override
    public Set<String> getOptions() {
        return Stream.of(Reply.LIKE, Reply.DISLIKE, Reply.SETTINGS)
                .map(Reply::toString)
                .collect(toSet());
    }
}
