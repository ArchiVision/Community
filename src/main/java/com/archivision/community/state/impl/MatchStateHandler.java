package com.archivision.community.state.impl;

import com.archivision.community.bot.State;
import com.archivision.community.cache.ActiveRegistrationProcessCache;
import com.archivision.community.cache.ActiveViewingData;
import com.archivision.community.matcher.MatchedUsersListResolver;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.model.Reply;
import com.archivision.community.service.*;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.state.WithReplyOptions;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (isLikedButtonPressed(message.getText())) {
            userInteractionService.handleLikeAction(chatId);
        } else {
            userInteractionService.handleDislikeAction(chatId);
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
    public State getState() {
        return State.MATCH;
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }

    public boolean isLikedButtonPressed(String msg) {
        return "+".equals(msg);
    }

    @Override
    public Set<String> getOptions() {
        return Set.of(Reply.LIKE.toString(), Reply.DISLIKE.toString());
    }
}
