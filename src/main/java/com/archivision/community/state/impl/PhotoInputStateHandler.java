package com.archivision.community.state.impl;

import com.archivision.community.bot.State;
import com.archivision.community.cache.ActiveRegistrationProcessCache;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.dto.UserDto;
import com.archivision.community.entity.User;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.TelegramImageS3Service;
import com.archivision.community.service.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

import static com.archivision.community.bot.State.AGE;
import static com.archivision.community.bot.State.APPROVE;

@Component
@Slf4j
public class PhotoInputStateHandler extends AbstractStateHandler {
    private final TelegramImageS3Service imageS3Service;

    public PhotoInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                  KeyboardBuilderService keyboardBuilder, TelegramImageS3Service imageS3Service,
                                  ActiveRegistrationProcessCache registrationProcessCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, registrationProcessCache);
        this.imageS3Service = imageS3Service;
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        UserDto user = registrationProcessCache.getCurrentUser(chatId);
        user.setPhotoId(TelegramImageS3Service.generateUserAvatarKey(chatId));
        imageS3Service.uploadImageToStorage(chatId, getFileId(message));
        goToApprovalState(chatId);
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Cannot send a photo");
    }

    @Override
    public boolean valid(Message message) {
        boolean ableToSendPhoto = isAbleToSendPhoto(message.getChatId(), message.getText());
        return ableToSendPhoto && message.hasPhoto();
    }

    private boolean isAbleToSendPhoto(Long chatId, String messageText) {
        boolean isStateChanged = changeStateToApprovalIfSkipped(chatId, messageText);
        return !isStateChanged;
    }

    @Override
    public State getStateType() {
        return State.PHOTO;
    }

    @Override
    public boolean isValidatable() {
        return true;
    }

    private boolean changeStateToApprovalIfSkipped(Long chatId, String messageText) {
        if ("Пропустити".equals(messageText)) {
            goToApprovalState(chatId);
            return true;
        }
        return false;
    }

    private void goToApprovalState(Long chatId) {
        registrationProcessCache.getCurrentUser(chatId).setState(APPROVE);
        messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.APPROVE_INPUT, keyboardBuilder.generateApprovalButtons());
    }

    private String getFileId(Message message) {
        List<PhotoSize> photo = message.getPhoto();
        PhotoSize photoSize;
        if (photo.size() > 2) {
            photoSize = photo.get(2);
        } else {
            photoSize = photo.get(0);
        }
        return photoSize.getFileId();
    }
}
