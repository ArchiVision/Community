package com.archivision.community.state.impl.initial;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.state.OptionalState;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.community.bot.UserFlowState.PHOTO;

@Component
@Slf4j
public class DescriptionStateHandler extends AbstractStateHandler implements OptionalState {

    public DescriptionStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                   KeyboardBuilderService keyboardBuilder, UserCache userCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, userCache);
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        userCache.processUser(chatId, userDto -> {
            userDto.setUserFlowState(PHOTO);
            userDto.setDescription(messageText);
        });
        messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.PHOTO, keyboardBuilder.skipButton());
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
        userCache.processUser(chatId, userDto -> userDto.setUserFlowState(nextState.userFlowState()));
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
