package com.archivision.community.service;

import com.archivision.community.bot.CommunityBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

@Component
@Slf4j
public class TelegramImageS3Service {
    @Value("${amazon.s3.bucket-name}")
    private String avatarsBucketName;
    private final CommunityBot communityBot;
    private final S3Service s3Service;
    private static final String PICTURE_PATH = "uploads/photos/";

    public TelegramImageS3Service(CommunityBot communityBot, S3Service s3Service) {
        this.communityBot = communityBot;
        this.s3Service = s3Service;
    }

    /**
     *
     * @param chatId user's chat id
     * @param fileId relative path on Telegram server
     */
    public void uploadImageToStorage(String chatId, String fileId) {
        String path = downloadFileById(chatId, fileId);
        s3Service.uploadFile(avatarsBucketName, generateUserAvatarKey(chatId), path);
    }

    /**
     *
     * @param chatId user's chat id. Will be used to generate key to get it from S3 bucket
     */
    public void sendImageToUser(Long chatId) {
        String pictureKey = generateUserAvatarKey(chatId);
        String downloadedPicturePath = PICTURE_PATH + pictureKey;
        s3Service.downloadFile(avatarsBucketName, pictureKey, downloadedPicturePath);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(Paths.get(downloadedPicturePath).toFile());
        sendPhoto.setPhoto(inputFile);
        try {
            communityBot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
            // TODO: 17.07.2023 custom exception
        }
    }

    private String downloadFileById(String chatId, String fileId) {
        GetFile getFileRequest = new GetFile();
        getFileRequest.setFileId(fileId);
        File execute;
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

    public static String generateUserAvatarKey(Long userId) {
        return generateUserAvatarKey(String.valueOf(userId));
    }
}
