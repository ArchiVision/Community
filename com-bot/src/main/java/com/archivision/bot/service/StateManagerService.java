package com.archivision.bot.service;


import com.archivision.bot.state.StateHandler;
import com.archivision.common.model.bot.UserFlowState;
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
        StateHandler stateHandler = statesHandlers.get(userFlowState);
        if (stateHandler != null) {
            stateHandler.handle(message);
        }
    }
}
