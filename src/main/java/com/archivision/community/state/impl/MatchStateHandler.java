package com.archivision.community.state.impl;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.model.Reply;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.ProfileSender;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserInteractionService;
import com.archivision.community.service.user.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.state.WithReplyOptions;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.archivision.community.model.Reply.*;
import static java.util.stream.Collectors.toSet;

@Component
@Slf4j
public class MatchStateHandler extends AbstractStateHandler implements WithReplyOptions {
    private final ProfileSender profileSender;
    private final Map<String, Consumer<Long>> messageHandlerStrategy;

    public MatchStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                             KeyboardBuilderService keyboardBuilder, UserCache userCache,
                             UserInteractionService userLikeService, ProfileSender profileSender) {
        super(inputValidator, userService, messageSender, keyboardBuilder, userCache);
        this.profileSender = profileSender;

        this.messageHandlerStrategy = Map.of(
                LIKE.toString(), userLikeService::handleLikeAction,
                DISLIKE.toString(), userLikeService::handleDislikeAction,
                SETTINGS.toString(), chatId -> {
                    messageSender.sendMsgWithMarkup(chatId, SETTINGS.toString(), keyboardBuilder.subscriptions());
                    userService.changeState(chatId, UserFlowState.SETTINGS);
                },
                STATS.toString(), chatId -> {
                    messageSender.sendMsgWithMarkup(chatId, STATS.toString(), keyboardBuilder.backButton());
                    userService.changeState(chatId, UserFlowState.STATS);
                }
        );
    }

    @Override
    public void doHandle(Message message) {
        messageHandlerStrategy
                .get(message.getText())
                .accept(message.getChatId());
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

    @Override
    public Set<String> getOptions() {
        return Stream.of(LIKE, DISLIKE, SETTINGS, STATS)
                .map(Reply::toString)
                .collect(toSet());
    }

    @Override
    public void onStateChanged(Long chatId) {
        profileSender.showProfile(chatId);
        messageSender.sendMsgWithMarkup(chatId, SEARCH.toString(), keyboardBuilder.matchButtons());
        profileSender.sendNextProfile(chatId);
    }
}
