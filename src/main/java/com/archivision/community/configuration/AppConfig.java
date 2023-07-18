package com.archivision.community.configuration;

import com.archivision.community.strategy.MessageStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public Map<String, MessageStrategy> messageStrategyMap(List<MessageStrategy> strategies) {
        Map<String, MessageStrategy> strategyMap = new HashMap<>();
        for (MessageStrategy strategy : strategies) {
            strategyMap.put(strategy.getClass().getSimpleName(), strategy);
        }
        return strategyMap;
    }
}
