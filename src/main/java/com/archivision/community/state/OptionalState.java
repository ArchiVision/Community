package com.archivision.community.state;

import com.archivision.community.bot.State;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface OptionalState {
    void changeToNextState(Long chatId);
    NextStateData getNextState();
    record NextStateData(State state, String responseText, ReplyKeyboardMarkup markup) {
    }
}