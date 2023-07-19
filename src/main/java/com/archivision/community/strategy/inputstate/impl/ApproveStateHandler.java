package com.archivision.community.strategy.inputstate.impl;

import com.archivision.community.bot.State;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.document.Topic;
import com.archivision.community.document.User;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.TelegramImageS3Service;
import com.archivision.community.service.UserService;
import com.archivision.community.strategy.inputstate.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ApproveStateHandler extends AbstractStateHandler {
    private final TelegramImageS3Service imageS3Service;
    public ApproveStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                               KeyboardBuilderService keyboardBuilder, TelegramImageS3Service imageS3Service) {
        super(inputValidator, userService, messageSender, keyboardBuilder);
        this.imageS3Service = imageS3Service;
    }

    @Override
    public void handle(Message message) {
        String messageText = message.getText();
        Long chatId = message.getChatId();
        if (messageText.equals("Так")) {
            showFilledProfile(chatId);
            changeStateToMatch(chatId);
        } else if (messageText.equals("Змінити")){
            changeStateToName(chatId);
        } else {
            log.error("??");
        }
    }

    private void changeStateToName(Long chatId) {
        userService.changeState(chatId, State.NAME);
        messageSender.sendTextMessage(chatId, ResponseTemplate.NAME_INPUT);
    }

    private void changeStateToMatch(Long chatId) {
        userService.changeState(chatId, State.MATCH);
    }

    private void showFilledProfile(Long chatId) {
        User user = userService.getUserByTgId(chatId);
        boolean hasPhoto = !(user.getPhotoId() == null);
        imageS3Service.sendImageToUser(chatId, hasPhoto);
        String formattedProfileText = """
                %s, %s, %s
                            
                Теми: %s
                            
                Опис: %s
                """.formatted(user.getName(), user.getAge(), user.getCity(), formatTopics(user.getTopics()), user.getDescription() == null ? "пусто" : user.getDescription());
        messageSender.sendTextMessage(chatId, formattedProfileText);
        log.info("showing profile");
    }

    private String formatTopics(List<Topic> topics) {
        return  "{" + topics.stream().map(Topic::getName).collect(Collectors.joining(",")) + "}";
    }

    @Override
    public State getStateType() {
        return State.APPROVE;
    }
}
