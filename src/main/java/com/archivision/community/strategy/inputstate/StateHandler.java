package com.archivision.community.strategy.inputstate;

import com.archivision.community.bot.State;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface StateHandler {
    void handle(Message message);
    State getStateType();
}
