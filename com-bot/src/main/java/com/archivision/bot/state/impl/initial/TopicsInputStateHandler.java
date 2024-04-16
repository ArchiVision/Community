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
import com.archivision.common.model.dto.TopicDto;
import com.archivision.common.model.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Set;

import static com.archivision.common.model.bot.UserFlowState.DESCRIPTION;


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
        messageSender.sendMsgWithMarkup(chatId, "Ще одну тему?", keyboardBuilder.button("Завершити"));
    }

    @Override
    public void onValidationError(Message message) {
        // TODO: 19.07.2023 change this message to smth diff
        log.error("Не валідний топік ?? topic");
    }

    @Override
    public boolean isInputValid(Message message) {
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
    public UserFlowState getState() {
        return UserFlowState.TOPIC;
    }

    @Override
    public void changeToNextState(Long chatId) {
        NextStateData nextState = getNextState();
        registrationProcessCache.getCurrentUser(chatId).setUserFlowState(nextState.userFlowState());
        messageSender.sendNextStateData(chatId, nextState);
    }

    @Override
    public NextStateData getNextState() {
        return new NextStateData(DESCRIPTION, ResponseTemplate.DESC_INPUT, keyboardBuilder.skipButton());
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }
}
