package com.archivision.community.strategy.inputstate.impl;

import com.archivision.community.bot.State;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.document.User;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.TelegramImageS3Service;
import com.archivision.community.service.UserService;
import com.archivision.community.strategy.inputstate.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import static com.archivision.community.bot.State.APPROVE;

@Component
@Slf4j
public class PhotoInputStateHandler extends AbstractStateHandler {
    private final TelegramImageS3Service imageS3Service;

    public PhotoInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                  KeyboardBuilderService keyboardBuilder, TelegramImageS3Service imageS3Service) {
        super(inputValidator, userService, messageSender, keyboardBuilder);
        this.imageS3Service = imageS3Service;
    }

    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        boolean ableToSendPhoto = isAbleToSendPhoto(chatId, messageText);
        if (ableToSendPhoto && message.hasPhoto()) {
            PhotoSize photoSize = message.getPhoto().get(2);
            imageS3Service.uploadImageToStorage(chatId, photoSize.getFileId());
            User user = userService.getUserByTgId(chatId);
            user.setPhotoId(TelegramImageS3Service.generateUserAvatarKey(chatId));
            userService.updateUser(user);
            goToApprovalState(chatId);
        } else {
            log.error("Cannot send a photo");
        }
    }

    private boolean isAbleToSendPhoto(Long chatId, String messageText) {
        boolean isStateChanged = changeStateToApprovalIfSkipped(chatId, messageText);
        return !isStateChanged;
    }

    private boolean changeStateToApprovalIfSkipped(Long chatId, String messageText) {
        if ("Пропустити".equals(messageText)) {
            goToApprovalState(chatId);
            return true;
        }
        return false;
    }

    private void goToApprovalState(Long chatId) {
        userService.changeState(chatId, APPROVE);
        messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.APPROVE_INPUT, keyboardBuilder.generateApprovalButtons());
    }

    @Override
    public State getStateType() {
        return State.PHOTO;
    }
}
