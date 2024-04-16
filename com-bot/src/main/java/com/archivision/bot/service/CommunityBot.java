package com.archivision.bot.service;

import com.archivision.bot.processor.UpdateProcessor;
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

    public void setUpdateProcessor(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }
}
