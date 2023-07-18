package com.archivision.community.strategy.impl;

import com.archivision.community.strategy.MessageStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
@RequiredArgsConstructor
public class PhotoMessageStrategy implements MessageStrategy {
    @Override
    public void handleMessage(Message message) {
        // Logic for handling photo message
    }

    @Override
    public boolean supports(Message message) {
        return message.hasPhoto();
    }
}
