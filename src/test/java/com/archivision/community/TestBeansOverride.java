package com.archivision.community;

import com.archivision.community.bot.CommunityBot;
import com.archivision.community.processor.UpdateProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Configuration
public class TestBeansOverride {

    @Component
    @Primary
    public class CommunityBotMock extends CommunityBot {
        @Override
        public void setUpdateProcessor(UpdateProcessor updateProcessor) {
            super.setUpdateProcessor(updateProcessor);
        }

        @Override
        public void onUpdateReceived(Update update) {
            super.getUpdateProcessor().processUpdate(update);
        }

        @Override
        public String getBotUsername() {
            return super.getBotUsername();
        }

        @Override
        public String getBotToken() {
            return super.getBotToken();
        }
    }
}
