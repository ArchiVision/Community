package com.archivision.community.bot;

import com.archivision.community.processor.UpdateProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
@Primary
public class BotStarter {
    private final CommunityBot communityBot;
    private final UpdateProcessor updateProcessor;

    @PostConstruct
    public void start() {
        communityBot.setUpdateProcessor(updateProcessor);
    }
}
