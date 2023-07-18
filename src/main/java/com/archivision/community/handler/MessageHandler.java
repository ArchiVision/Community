package com.archivision.community.handler;

import com.archivision.community.strategy.MessageStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageHandler implements Handler<Message> {
    private final Map<String, MessageStrategy> strategyMap;

    public void handle(Message message) {
        log.info("New message={} from={}. Delegating message to appropriate handler..", message, message.getFrom());
        for (String strategyKey : strategyMap.keySet()) {
            MessageStrategy strategy = strategyMap.get(strategyKey);
            if (strategy.supports(message)) {
                strategy.handleMessage(message);
                return;
            }
        }
        log.error("Cannot find handler for message={}", message);
    }
}
