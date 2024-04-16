package com.archivision.bot.state.impl.initial;


import com.archivision.bot.cache.ActiveRegistrationProcessCache;
import com.archivision.bot.command.ResponseTemplate;
import com.archivision.bot.sender.MessageSender;
import com.archivision.bot.service.KeyboardBuilderService;
import com.archivision.bot.service.TelegramImageS3Service;
import com.archivision.bot.service.user.UserService;
import com.archivision.bot.state.AbstractStateHandler;
import com.archivision.bot.util.InputValidator;
import com.archivision.common.model.bot.UserFlowState;
import com.archivision.common.model.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

import static com.archivision.common.model.bot.UserFlowState.APPROVE;


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
        registrationProcessCache.getCurrentUser(chatId).setUserFlowState(APPROVE);
        messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.APPROVE_INPUT, keyboardBuilder.approvalButtons());
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
