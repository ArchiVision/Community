package com.archivision.community.state;

import com.archivision.community.bot.State;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface StateHandler {
    void handle(Message message);
    State getStateType();
}
