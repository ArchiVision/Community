package com.archivision.community.state.impl.initial;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.dto.TopicDto;
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

import java.util.Set;

import static com.archivision.community.bot.UserFlowState.DESCRIPTION;

@Component
@Slf4j
public class TopicsInputStateHandler extends AbstractStateHandler implements OptionalState {

    public TopicsInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                   KeyboardBuilderService keyboardBuilderService, UserCache userCache) {
        super(inputValidator, userService, messageSender, keyboardBuilderService, userCache);
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        userCache.processUser(chatId, userDto -> userDto.getTopics().add(new TopicDto().setName(messageText)));
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
        userService.changeState(chatId, nextState.userFlowState());
    }

    @Override
    public NextStateData getNextState() {
        return new NextStateData(DESCRIPTION, ResponseTemplate.DESC_INPUT, keyboardBuilder.skipButton());
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }

    @Override
    public void onStateChanged(Long chatId) {
        messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.TOPICS_INPUT,
                keyboardBuilder.skipButton());
    }
}
