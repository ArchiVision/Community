package com.archivision.community.state.input.impl;

import com.archivision.community.bot.State;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.entity.User;
import com.archivision.community.matcher.MatchedUsersListResolver;
import com.archivision.community.matcher.model.UserWithMatchedProbability;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.ProfileSender;
import com.archivision.community.service.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ApproveStateHandler extends AbstractStateHandler {
    private final ProfileSender profileSender;
    private final MatchedUsersListResolver matchedUsersListResolver;
    private final RedisTemplate<Long, String> redisTemplate;
    public ApproveStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                               KeyboardBuilderService keyboardBuilder, ProfileSender profileSender, MatchedUsersListResolver matchedUsersListResolver, RedisTemplate<Long, String> redisTemplate) {
        super(inputValidator, userService, messageSender, keyboardBuilder);
        this.profileSender = profileSender;
        this.matchedUsersListResolver = matchedUsersListResolver;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        if (messageText.equals("Так")) {
            profileSender.showProfile(chatId);
            changeStateToMatch(chatId);
            giveUserPersonList(chatId)
                    .ifPresent(user -> profileSender.showProfileOfUserTo(user.getTelegramUserId(), chatId));
        } else if (messageText.equals("Змінити")){
            changeStateToName(chatId);
        } else {
            log.error("??");
        }
    }

    @SneakyThrows
    private Optional<User> giveUserPersonList(Long chatId) {
        User user = userService.getUserByTgId(chatId);
        List<User> allUsers = userService.findAllExceptId(chatId);
        List<User> orderedMatchingList = matchedUsersListResolver.getOrderedMatchingList(user, allUsers).stream()
                .map(UserWithMatchedProbability::user)
                .collect(Collectors.toList());
        if (orderedMatchingList.size() > 0) {
            User remove = orderedMatchingList.get(0);
            redisTemplate.opsForValue().setIfAbsent(chatId, new ObjectMapper().writeValueAsString(orderedMatchingList));
            return Optional.of(remove);
        }
        return Optional.empty();
    }

    private void changeStateToName(Long chatId) {
        userService.changeState(chatId, State.NAME);
        messageSender.sendTextMessage(chatId, ResponseTemplate.NAME_INPUT);
    }

    private void changeStateToMatch(Long chatId) {
        userService.changeState(chatId, State.MATCH);
        messageSender.sendMsgWithMarkup(chatId, "Пошук", ReplyKeyboardMarkup.builder()
                        .resizeKeyboard(true)
                        .keyboardRow(new KeyboardRow(){{
                            add("+");
                            add("-");
                        }})
                .build());
    }

    @Override
    public State getStateType() {
        return State.APPROVE;
    }

    @Override
    public boolean isValidatable() {
        return false;
    }
}
