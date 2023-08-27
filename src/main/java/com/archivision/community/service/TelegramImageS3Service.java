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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component
@Slf4j
public class TelegramImageS3Service {
    @Value("${amazon.s3.bucket-name}")
    private String avatarsBucketName;
    private final CommunityBot communityBot;
    private final S3Service s3Service;
    private static final String PICTURE_PATH = "uploads/photos/";
    @Value("#{resourceLoaderService.getDefaultImage()}")
    private byte[] defaultImageBytes;

    public TelegramImageS3Service(CommunityBot communityBot, S3Service s3Service) {
        this.communityBot = communityBot;
        this.s3Service = s3Service;
    }

    /**
     *
     * @param chatId user's chat id
     * @param fileId relative path on Telegram server
     */
    public void uploadImageToStorage(Long chatId, String fileId) {
        byte[] bytes = downloadFileById(fileId);
        s3Service.uploadFileAsBytes(avatarsBucketName, generateUserAvatarKey(chatId), bytes);
    }

    /**
     *
     * @param chatId user's chat id. Will be used to generate key to get it from S3 bucket
     */
    public void sendImageOfUserToUser(Long userId, Long toUser, boolean hasPhoto) {
        sendImageOfUserToUser(userId, toUser, hasPhoto, null);
    }

    public void sendImageOfUserToUser(Long userId, Long toUser, boolean hasPhoto, String caption) {
        String pictureKey = generateUserAvatarKey(userId);
        byte[] image = defaultImageBytes;
        if (hasPhoto) {
            image = s3Service.downloadFileAsBytes(avatarsBucketName, pictureKey);
        }
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(toUser);
        if (caption != null) {
            sendPhoto.setCaption(caption);
        }
        InputFile inputFile = new InputFile();
        InputStream inputStream = new ByteArrayInputStream(image);
        inputFile.setMedia(inputStream, PICTURE_PATH + pictureKey);
        sendPhoto.setPhoto(inputFile);
        try {
            communityBot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
            // TODO: 17.07.2023 custom exception
        }
    }

    private byte[] downloadFileById(String fileId) {
        GetFile getFileRequest = new GetFile();
        getFileRequest.setFileId(fileId);
        File execute;
        try {
            execute = communityBot.execute(getFileRequest);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
            // TODO: 17.07.2023 custom exception
        }
        return downloadFileByFilePath(execute.getFilePath());
    }

    private byte[] downloadFileByFilePath(String fileId) {
        String fileUrl = String.format("https://api.telegram.org/file/bot%s/%s", communityBot.getBotToken(), fileId);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (InputStream inputStream = new URL(fileUrl).openStream()) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(dataBuffer, 0, 1024)) != -1) {
                byteStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
            // TODO: 17.07.2023 custom exception
        }
        return byteStream.toByteArray();
    }

    public static String generateUserAvatarKey(String userId) {
        return "avatars_" + userId + ".jpg";
    }

    public static String generateUserAvatarKey(Long userId) {
        return generateUserAvatarKey(String.valueOf(userId));
    }
}
