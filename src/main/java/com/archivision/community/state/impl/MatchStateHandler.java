package com.archivision.community.state.impl;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.model.Reply;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserInteractionService;
import com.archivision.community.service.user.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.state.WithReplyOptions;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Component
@Slf4j
public class MatchStateHandler extends AbstractStateHandler implements WithReplyOptions {
    private final UserInteractionService userInteractionService;

    public MatchStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                             KeyboardBuilderService keyboardBuilder, UserCache userCache,
                             UserInteractionService userLikeService) {
        super(inputValidator, userService, messageSender, keyboardBuilder, userCache);
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
        if (messageText.equals(Reply.STATS.toString())) {
            // how to trigger execution of another state immediayl (without sending explicit message)
            messageSender.sendMsgWithMarkup(chatId, "Статистика", keyboardBuilder.backButton());
            userService.changeState(chatId, UserFlowState.STATS);
        }
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Validation error in state: {}. Message={}", getState(), message.getText());
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
        return Stream.of(Reply.LIKE, Reply.DISLIKE, Reply.SETTINGS, Reply.STATS)
                .map(Reply::toString)
                .collect(toSet());
    }
}
