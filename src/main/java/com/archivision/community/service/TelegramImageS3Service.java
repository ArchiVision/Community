package com.archivision.community.service;

import com.archivision.community.bot.CommunityBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@Component
@Slf4j
public class TelegramImageS3Service {
    @Value("${amazon.s3.bucket-name}")
    private String avatarsBucketName;

    private final CommunityBot communityBot;
    private final S3Service s3Service;

    public TelegramImageS3Service(@Lazy CommunityBot communityBot, S3Service s3Service) {
        this.communityBot = communityBot;
        this.s3Service = s3Service;
    }

    public void uploadImage(String chatId, String fileId) {
        // filePath - relative path on Telegram server
        String path = downloadFileById(chatId, fileId);
        s3Service.uploadFile(avatarsBucketName, generateUserAvatarKey(chatId), path);
    }

    private String downloadFileById(String chatId, String fileId) {
        GetFile getFileRequest = new GetFile();
        getFileRequest.setFileId(fileId);
        File execute = null;
        try {
            execute = communityBot.execute(getFileRequest);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
            // TODO: 17.07.2023 custom exception
        }
        return downloadFileByFilePath(execute.getFilePath(), chatId);
    }

    private String downloadFileByFilePath(String fileId, String userId) {
        String fileUrl = String.format("https://api.telegram.org/file/bot%s/%s", communityBot.getBotToken(), fileId);
        String fileName = "uploads/photos/avatar_" + userId + ".jpg";
        try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
            // TODO: 17.07.2023 custom exception
        }
        return fileName;
    }

    public static String generateUserAvatarKey(String userId) {
        return "avatars_" + userId + ".jpg";
    }
}
