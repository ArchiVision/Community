package com.archivision.community.bot;

import com.archivision.community.receiver.UpdateReceiver;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
@Primary
public class BotStarter {
    private final BotRegistrar botRegistrar;
    private final BroadcasterBot broadcasterBot;
    private final UpdateReceiver updateReceiver;

    @PostConstruct
    public void start() throws TelegramApiException {
        broadcasterBot.setUpdateHandler(updateReceiver);
        botRegistrar.register(broadcasterBot);
    }
}
