package com.archivision.community.strategy.inputstate.impl;

import com.archivision.community.bot.State;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.document.Topic;
import com.archivision.community.document.User;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.strategy.inputstate.AbstractStateHandler;
import com.archivision.community.strategy.inputstate.OptionalState;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Set;

import static com.archivision.community.bot.State.DESCRIPTION;

@Component
@Slf4j
public class TopicsInputStateHandler extends AbstractStateHandler implements OptionalState {

    public TopicsInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                   KeyboardBuilderService keyboardBuilderService) {
        super(inputValidator, userService, messageSender, keyboardBuilderService);
    }

    @Override
    public void handle(Message message) {
        String messageText = message.getText();
        Long chatId = message.getChatId();
        boolean ableToInputTopic = isAbleToInputTopic(chatId, messageText);
        if (ableToInputTopic && inputValidator.isTopicValid(messageText)) {
            User userByTgId = userService.getUserByTgId(chatId);
            Topic topic = new Topic();
            topic.setName(messageText);
            String topicId = userService.addTopic(topic).getId();
            userService.addTopicToUser(userByTgId.getId(), topicId);
            log.info("topic={}", messageText);
            messageSender.sendMsgWithMarkup(chatId, "Ще одну тему?", keyboardBuilder.buildButtonWithText("Завершити"));
        } else {
            // TODO: 19.07.2023 change this message to smth diff
            log.error("Не валідний топік ?? topic");
        }
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
        userService.changeState(chatId, nextState.state());
        messageSender.sendNextStateData(chatId, nextState);
    }

    @Override
    public NextStateData getNextState() {
        return new NextStateData(DESCRIPTION, ResponseTemplate.DESC_INPUT, keyboardBuilder.generateSkipButton());
    }
}
