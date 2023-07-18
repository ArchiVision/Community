package com.archivision.community.strategy.inputstate;

import com.archivision.community.bot.State;
import com.archivision.community.document.User;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface StateHandler {
    void handle(User user, Message message);
    State getStateType();
}
