package com.archivision.bot.state;

import com.archivision.common.model.bot.UserFlowState;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface StateHandler {
    void handle(Message message);
    UserFlowState getState();
}
