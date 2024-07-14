package com.archivision.community;

import com.archivision.community.bot.CommunityBot;
import com.archivision.community.processor.UpdateProcessor;
import com.archivision.community.service.S3Service;
import com.archivision.community.service.TelegramImageS3Service;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Configuration
public class TestBeansOverride {
    @Component
    @Primary
    public class TelegramImageServiceMock extends TelegramImageS3Service {
        public TelegramImageServiceMock(CommunityBot communityBot, S3Service s3Service) {
            super(communityBot, s3Service);
        }

        @Override
        public void uploadImageToStorage(Long chatId, String fileId) {
            super.uploadImageToStorage(chatId, fileId);
        }

        @Override
        public void sendImageOfUserToUser(Long userId, Long toUser, boolean hasPhoto, String caption) {
            // Ignore
        }
    }

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
