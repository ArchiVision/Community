package com.archivision.community.configuration;

import com.archivision.community.bot.State;
import com.archivision.community.state.StateHandler;
import com.archivision.community.strategy.message.MessageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final List<StateHandler> stateHandlers;

    @Bean
    public Map<String, MessageStrategy> messageStrategyMap(List<MessageStrategy> strategies) {
        Map<String, MessageStrategy> strategyMap = new HashMap<>();
        for (MessageStrategy strategy : strategies) {
            strategyMap.put(strategy.getClass().getSimpleName(), strategy);
        }
        return strategyMap;
    }

    @Bean
    public Map<State, StateHandler> stateStateHandlerMap() {
        Map<State, StateHandler> map = new HashMap<>();
        for (StateHandler stateHandler : stateHandlers) {
            map.put(stateHandler.getStateType(), stateHandler);
        }
        return map;
    }
}
