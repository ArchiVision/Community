package com.archivision.community.service;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.state.StateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class StateManagerService {
    private final Map<UserFlowState, StateHandler> statesHandlers;

    public void manageOtherStates(UserFlowState userFlowState, Message message) {
        final StateHandler stateHandler = statesHandlers.get(userFlowState);
        if (stateHandler != null) {
            stateHandler.handle(message);
        }
    }

    public StateHandler getStateHandler(UserFlowState userFlowState) {
        return statesHandlers.get(userFlowState);
    }
}
