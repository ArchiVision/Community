package com.archivision.community.strategy.inputstate.impl;

import com.archivision.community.bot.State;
import com.archivision.community.matcher.MatchedUsersListResolver;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.strategy.inputstate.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class MatchStateHandler extends AbstractStateHandler {
    private final MatchedUsersListResolver matchedUsersListResolver;

    public MatchStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                             KeyboardBuilderService keyboardBuilder, MatchedUsersListResolver matchedUsersListResolver) {
        super(inputValidator, userService, messageSender, keyboardBuilder);

        this.matchedUsersListResolver = matchedUsersListResolver;
    }

    @Override
    public void handle(Message message) {

    }

    @Override
    public State getStateType() {
        return State.MATCH;
    }
}
