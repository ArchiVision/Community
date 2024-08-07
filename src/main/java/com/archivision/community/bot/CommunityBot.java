package com.archivision.community.bot;

import com.archivision.community.processor.UpdateProcessor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class CommunityBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String tgUsername;
    @Value("${telegram.bot.token}")
    private String tgToken;

    @Setter
    @Getter
    private UpdateProcessor updateProcessor;

    @Override
    public void onUpdateReceived(Update update) {
        updateProcessor.processUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return tgUsername;
    }

    @Override
    public String getBotToken() {
        return tgToken;
    }
}
