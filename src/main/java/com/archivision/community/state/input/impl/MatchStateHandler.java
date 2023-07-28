package com.archivision.community.state.input.impl;

import com.archivision.community.bot.State;
import com.archivision.community.matcher.MatchedUsersListResolver;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.state.AbstractStateHandler;
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
    public void doHandle(Message message) {
        // TODO: 28.07.2023
    }

    @Override
    public void onValidationError(Message message) {
        super.onValidationError(message);
        // TODO: 28.07.2023
    }

    @Override
    public boolean valid(Message message) {
        return super.valid(message);
        // TODO: 28.07.2023
    }

    @Override
    public State getStateType() {
        return State.MATCH;
    }

    @Override
    public boolean isValidatable() {
        return true;
    }
}
