package com.archivision.community.bot;

import com.archivision.community.processor.UpdateProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommunityBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String TELEGRAM_BOT_USERNAME;
    @Value("${telegram.bot.token}")
    private String TELEGRAM_BOT_TOKEN;

    private UpdateProcessor updateProcessor;

    @Override
    public void onUpdateReceived(Update update) {
        updateProcessor.processUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return TELEGRAM_BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return TELEGRAM_BOT_TOKEN;
    }

    @Autowired
    public void setUpdateHandler(UpdateProcessor updateReceiver) {
        this.updateProcessor = updateReceiver;
    }
}
