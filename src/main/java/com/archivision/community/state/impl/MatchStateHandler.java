package com.archivision.community.state.impl;

import com.archivision.community.bot.State;
import com.archivision.community.cache.ActiveViewingData;
import com.archivision.community.matcher.MatchedUsersListResolver;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.ProfileSender;
import com.archivision.community.service.UserLikeService;
import com.archivision.community.service.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class MatchStateHandler extends AbstractStateHandler {
    private final MatchedUsersListResolver matchedUsersListResolver;
    private final ActiveViewingData activeViewingData;
    private final UserLikeService userLikeService;
    private final ProfileSender profileSender;
    private static final Set<String> options = Set.of("+", "-");

    public MatchStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                             KeyboardBuilderService keyboardBuilder, MatchedUsersListResolver matchedUsersListResolver, ActiveViewingData activeViewingData, UserLikeService userLikeService, ProfileSender profileSender) {
        super(inputValidator, userService, messageSender, keyboardBuilder);

        this.matchedUsersListResolver = matchedUsersListResolver;
        this.activeViewingData = activeViewingData;
        this.userLikeService = userLikeService;
        this.profileSender = profileSender;
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        if (isLikedButtonPressed(message.getText())) {
            log.info("Like");
            Optional<Long> viewingUser = activeViewingData.get(message.getChatId());
            viewingUser.ifPresent(checkingThisUser -> {
                activeViewingData.remove(chatId);
                userLikeService.like(chatId, checkingThisUser);
            });
            profileSender.sendNextProfile(chatId);
        } else {
            log.info("dislike");
            // smth else
        }
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Smth went wrong. Message={}", message.getText());
    }

    @Override
    public boolean valid(Message message) {
        return options.contains(message.getText());
    }

    @Override
    public State getStateType() {
        return State.MATCH;
    }

    @Override
    public boolean isValidatable() {
        return true;
    }

    public boolean isLikedButtonPressed(String msg) {
        return "+".equals(msg);
    }
}
