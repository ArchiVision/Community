package com.archivision.community.state.impl;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserService;
import com.archivision.community.service.user.UserStatsService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.state.Validatable;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class StatsStateHandler extends AbstractStateHandler implements Validatable {
    private final String STATS_TEMPLATE =
            """
            Popularity index: %s
            """;

    private UserStatsService userStatsService;

    public StatsStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                             KeyboardBuilderService keyboardBuilder, UserCache userCache, UserStatsService userStatsService) {
        super(inputValidator, userService, messageSender, keyboardBuilder, userCache);
        this.userStatsService = userStatsService;
    }

    @Override
    public void doHandle(Message message) {
        final Long chatId = message.getChatId();

        messageSender.sendMsgWithMarkup(chatId, getStatsResponseMessage(), keyboardBuilder.button("Завершити"));
        userService.changeState(chatId, UserFlowState.STATS);
    }

    private String getStatsResponseMessage() {
        return String.format(STATS_TEMPLATE, userStatsService.resolveUserPopularityIndex());
    }

    @Override
    public UserFlowState getState() {
        return UserFlowState.STATS;
    }

    @Override
    public boolean shouldValidateInput() {
        return false;
    }
}
