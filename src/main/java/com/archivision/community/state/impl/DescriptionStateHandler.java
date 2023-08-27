package com.archivision.community.state.impl;

import com.archivision.community.bot.State;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.entity.User;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.state.OptionalState;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.community.bot.State.PHOTO;

@Component
@Slf4j
public class DescriptionStateHandler extends AbstractStateHandler implements OptionalState {

    public DescriptionStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                   KeyboardBuilderService keyboardBuilder) {
        super(inputValidator, userService, messageSender, keyboardBuilder);
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        User user = userService.getUserByTgId(chatId);
        user.setState(PHOTO);
        user.setDescription(messageText);
        userService.updateUser(user);
        messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.PHOTO, keyboardBuilder.generateSkipButton());
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
    public boolean valid(Message message) {
        boolean ableToEnter = isAbleToEnterDesc(message.getChatId(), message.getText());
        return ableToEnter && inputValidator.isDescriptionValid(message.getText());
    }

    @Override
    public State getStateType() {
        return State.DESCRIPTION;
    }

    @Override
    public void changeToNextState(Long chatId) {
        NextStateData nextStateData = getNextState();
        userService.changeState(chatId, nextStateData.state());
        messageSender.sendNextStateData(chatId, nextStateData);
    }

    @Override
    public NextStateData getNextState() {
        return new NextStateData(PHOTO, ResponseTemplate.PHOTO, keyboardBuilder.generateSkipButton());
    }

    @Override
    public boolean isValidatable() {
        return true;
    }
}
