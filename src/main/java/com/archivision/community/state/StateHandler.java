package com.archivision.community.state;

import com.archivision.community.bot.UserFlowState;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface StateHandler {
    void handle(Message message);
    UserFlowState getState();
    default void onStateChanged(Long chatId) {
        // nothing
    }
}
