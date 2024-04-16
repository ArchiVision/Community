package com.archivision.bot.state.impl.initial;


import com.archivision.bot.cache.ActiveRegistrationProcessCache;
import com.archivision.bot.command.ResponseTemplate;
import com.archivision.bot.sender.MessageSender;
import com.archivision.bot.service.KeyboardBuilderService;
import com.archivision.bot.service.user.UserService;
import com.archivision.bot.state.AbstractStateHandler;
import com.archivision.bot.state.OptionalState;
import com.archivision.bot.util.InputValidator;
import com.archivision.common.model.bot.UserFlowState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.common.model.bot.UserFlowState.PHOTO;

@Component
@Slf4j
public class DescriptionStateHandler extends AbstractStateHandler implements OptionalState {

    public DescriptionStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                   KeyboardBuilderService keyboardBuilder, ActiveRegistrationProcessCache registrationProcessCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, registrationProcessCache);
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        registrationProcessCache.get(chatId).ifPresent(user -> {
            String messageText = message.getText();
            user.setUserFlowState(PHOTO);
            user.setDescription(messageText);
            messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.PHOTO, keyboardBuilder.skipButton());
        });
    }

    private boolean isAbleToEnterDesc(Long chatId, String messageText) {
        boolean isStateChanged = changeStateToPhotoIfSkipped(chatId, messageText);
        return !isStateChanged;
    }

    private boolean changeStateToPhotoIfSkipped(Long chatId, String messageText) {
        if ("Пропустити".equals(messageText)) {
            changeToNextState(chatId);
            return true;
        }
        return false;
    }

    @Override
    public void onValidationError(Message message) {
        // TODO: 19.07.2023 change this message to smth diff
        log.error("?? desc={}", message.getText());
    }

    @Override
    public boolean isInputValid(Message message) {
        boolean ableToEnter = isAbleToEnterDesc(message.getChatId(), message.getText());
        return ableToEnter && inputValidator.isDescriptionValid(message.getText());
    }

    @Override
    public UserFlowState getState() {
        return UserFlowState.DESCRIPTION;
    }

    @Override
    public void changeToNextState(Long chatId) {
        NextStateData nextState = getNextState();
        registrationProcessCache.getCurrentUser(chatId).setUserFlowState(nextState.userFlowState());
        messageSender.sendNextStateData(chatId, nextState);
    }

    @Override
    public NextStateData getNextState() {
        return new NextStateData(PHOTO, ResponseTemplate.PHOTO, keyboardBuilder.skipButton());
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }
}
