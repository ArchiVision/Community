package com.archivision.community.bot;

import com.archivision.community.processor.UpdateProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class CommunityBot extends TelegramWebhookBot {
    @Value("${telegram.bot.username}")
    private String tgUsername;
    @Value("${telegram.bot.token}")
    private String tgToken;
    @Value("${telegram.bot.webhook-path}")
    private String webhookPath;
    private UpdateProcessor updateProcessor;

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

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        updateProcessor.processUpdate(update);
        return null;
    }

    @Override
    public String getBotPath() {
        return webhookPath;
    }
}
