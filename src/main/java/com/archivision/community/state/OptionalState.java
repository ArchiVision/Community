package com.archivision.community.state;

import com.archivision.community.bot.UserFlowState;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface OptionalState {
    void changeToNextState(Long chatId);
    NextStateData getNextState();
    record NextStateData(UserFlowState userFlowState, String responseText, ReplyKeyboardMarkup markup) {
    }
}