package com.archivision.community;

import com.archivision.community.bot.CommunityBot;
import com.archivision.community.processor.UpdateProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Configuration
@Slf4j
public class TestBeansOverrides {
    @Component
    @Primary
    public class CommunityBotMock extends CommunityBot {
        private Update latestUpdateToBeConsumed;

        @Override
        public void setUpdateProcessor(UpdateProcessor updateProcessor) {
            super.setUpdateProcessor(updateProcessor);
        }

        @Override
        public void onUpdateReceived(Update update) {
            if (latestUpdateToBeConsumed == null) {
                log.info("There is no new updates");
            } else {
                getUpdateProcessor().processUpdate(latestUpdateToBeConsumed);
                latestUpdateToBeConsumed = null;
            }
        }

        @Override
        public String getBotUsername() {
            return super.getBotUsername();
        }

        @Override
        public String getBotToken() {
            return super.getBotToken();
        }

        public void addUpdate(Update update) {
            this.latestUpdateToBeConsumed = update;
        }
    }
}
