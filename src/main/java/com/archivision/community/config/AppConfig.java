package com.archivision.community.config;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.state.StateHandler;
import com.archivision.community.strategy.message.MessageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Map<String, MessageStrategy> userMessageStrategyMap(List<MessageStrategy> strategies) {
        Map<String, MessageStrategy> strategyMap = new HashMap<>();
        for (MessageStrategy strategy : strategies) {
            strategyMap.put(strategy.getClass().getSimpleName(), strategy);
        }
        return strategyMap;
    }

    @Bean
    public Map<UserFlowState, StateHandler> userFlowStateStrategyMap() {
        Map<UserFlowState, StateHandler> map = new HashMap<>();
        for (StateHandler stateHandler : stateHandlers) {
            map.put(stateHandler.getState(), stateHandler);
        }
        return map;
    }
}
