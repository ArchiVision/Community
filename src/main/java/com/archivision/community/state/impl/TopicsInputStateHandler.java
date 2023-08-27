package com.archivision.community.state.impl;

import com.archivision.community.bot.State;
import com.archivision.community.cache.ActiveRegistrationProcessCache;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.dto.TopicDto;
import com.archivision.community.dto.UserDto;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.state.OptionalState;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Set;

import static com.archivision.community.bot.State.APPROVE;
import static com.archivision.community.bot.State.DESCRIPTION;

@Component
@Slf4j
public class TopicsInputStateHandler extends AbstractStateHandler implements OptionalState {

    public TopicsInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                   KeyboardBuilderService keyboardBuilderService, ActiveRegistrationProcessCache registrationProcessCache) {
        super(inputValidator, userService, messageSender, keyboardBuilderService, registrationProcessCache);
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        UserDto user = registrationProcessCache.getCurrentUser(chatId);
        String messageText = message.getText();
        user.getTopics().add(new TopicDto().setName(messageText));
        log.info("topic={}", messageText);
        messageSender.sendMsgWithMarkup(chatId, "Ще одну тему?", keyboardBuilder.buildButtonWithText("Завершити"));
    }

    @Override
    public void onValidationError(Message message) {
        // TODO: 19.07.2023 change this message to smth diff
        log.error("Не валідний топік ?? topic");
    }

    @Override
    public boolean valid(Message message) {
        boolean ableToInputTopic = isAbleToInputTopic(message.getChatId(), message.getText());
        return ableToInputTopic && inputValidator.isTopicValid(message.getText());
    }

    private boolean isAbleToInputTopic(Long chatId, String messageText) {
        boolean isStateChanged = changeStateToApproveIfSkipped(chatId, messageText);
        return !isStateChanged;
    }

    private boolean changeStateToApproveIfSkipped(Long chatId, String messageText) {
        // TODO: 19.07.2023 move this Set creating to constant
        if (Set.of("Пропустити", "Завершити").contains(messageText)) {
            changeToNextState(chatId);
            return true;
        }
        return false;
    }

    @Override
    public State getStateType() {
        return State.TOPIC;
    }

    @Override
    public void changeToNextState(Long chatId) {
        NextStateData nextState = getNextState();
        registrationProcessCache.getCurrentUser(chatId).setState(nextState.state());
        messageSender.sendNextStateData(chatId, nextState);
    }

    @Override
    public NextStateData getNextState() {
        return new NextStateData(DESCRIPTION, ResponseTemplate.DESC_INPUT, keyboardBuilder.generateSkipButton());
    }

    @Override
    public boolean isValidatable() {
        return true;
    }
}
