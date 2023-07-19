package com.archivision.community.strategy.inputstate.impl;

import com.archivision.community.bot.State;
import com.archivision.community.document.User;
import com.archivision.community.matcher.MatchedUsersListResolver;
import com.archivision.community.matcher.model.UserWithMatchedProbability;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.strategy.inputstate.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

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
        final Long chatId = message.getChatId();
        final User user = userService.getUserByTgId(chatId);
        final List<User> allUsers = userService.findAllExceptId(chatId);

        final List<UserWithMatchedProbability> orderedMatchingList = matchedUsersListResolver.getOrderedMatchingList(user, allUsers);
        // todo: send by one

    }

    @Override
    public State getStateType() {
        return State.MATCH;
    }
}
