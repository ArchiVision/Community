package com.archivision.community.service;

import com.archivision.community.bot.State;
import com.archivision.community.document.User;
import com.archivision.community.strategy.inputstate.StateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class StateManagerService {
    private final Map<State, StateHandler> statesHandlers;

    public void manageOtherStates(User user, Message message) {
        StateHandler stateHandler = statesHandlers.get(user.getState());
        if (stateHandler != null) {
            stateHandler.handle(user, message);
        }
    }
}
