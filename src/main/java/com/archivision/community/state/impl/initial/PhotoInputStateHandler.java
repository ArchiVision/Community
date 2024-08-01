package com.archivision.community.state.impl.initial;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.TelegramImageS3Service;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

import static com.archivision.community.bot.UserFlowState.APPROVE;

@Component
@Slf4j
public class PhotoInputStateHandler extends AbstractStateHandler {
    private final TelegramImageS3Service imageS3Service;

    public PhotoInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                  KeyboardBuilderService keyboardBuilder, TelegramImageS3Service imageS3Service,
                                  UserCache userCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, userCache);
        this.imageS3Service = imageS3Service;
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        userCache.processUser(chatId, userDto -> userDto.setPhotoId(TelegramImageS3Service.generateUserAvatarKey(chatId)));
        imageS3Service.uploadImageToStorage(chatId, getFileId(message));
        goToApprovalState(chatId);
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Cannot send a photo");
    }

    @Override
    public boolean isInputValid(Message message) {
        boolean ableToSendPhoto = isAbleToSendPhoto(message.getChatId(), message.getText());
        return ableToSendPhoto && message.hasPhoto();
    }

    private boolean isAbleToSendPhoto(Long chatId, String messageText) {
        boolean isStateChanged = changeStateToApprovalIfSkipped(chatId, messageText);
        return !isStateChanged;
    }

    @Override
    public UserFlowState getState() {
        return UserFlowState.PHOTO;
    }

    @Override
    public boolean shouldValidateInput() {
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
        userService.changeState(chatId, APPROVE);
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

    @Override
    public void onStateChanged(Long chatId) {
        messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.PHOTO, keyboardBuilder.skipButton());
    }
}
