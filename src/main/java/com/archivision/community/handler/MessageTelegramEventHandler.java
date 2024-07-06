package com.archivision.community.handler;

import com.archivision.community.strategy.message.MessageStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

/**
 * General handler that define specific message strategy(text/photo) to process a message
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MessageTelegramEventHandler implements TelegramEventHandler<Message> {
    private final Map<String, MessageStrategy> strategyMap;

    public void handle(Message message) {
        log.info("Message: messageId: {}, userId: {}, text: {}", message.getMessageId(), message.getFrom().getId(), message.getText());

        for (String strategyKey : strategyMap.keySet()) {
            final MessageStrategy strategy = strategyMap.get(strategyKey);
            if (strategy.supports(message)) {
                strategy.handleMessage(message);
                return;
            }
        }
    }
}
