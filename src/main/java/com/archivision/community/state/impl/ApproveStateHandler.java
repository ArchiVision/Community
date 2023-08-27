package com.archivision.community.state.impl;

import com.archivision.community.bot.State;
import com.archivision.community.cache.ActiveRegistrationProcessCache;
import com.archivision.community.cache.ActiveViewingData;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.dto.UserDto;
import com.archivision.community.matcher.MatchedUsersListResolver;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.ProfileSender;
import com.archivision.community.service.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
@Slf4j
public class ApproveStateHandler extends AbstractStateHandler {
    private final ProfileSender profileSender;
    private final MatchedUsersListResolver matchedUsersListResolver;
    private final RedisTemplate<Long, String> redisTemplate;
    private final ActiveViewingData activeViewingData;
    private final ActiveRegistrationProcessCache registrationProcessCache;

    public ApproveStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                               KeyboardBuilderService keyboardBuilder, ProfileSender profileSender, MatchedUsersListResolver matchedUsersListResolver, RedisTemplate<Long, String> redisTemplate, ActiveViewingData activeViewingData,
                               ActiveRegistrationProcessCache registrationProcessCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, registrationProcessCache);
        this.profileSender = profileSender;
        this.matchedUsersListResolver = matchedUsersListResolver;
        this.redisTemplate = redisTemplate;
        this.activeViewingData = activeViewingData;
        this.registrationProcessCache = registrationProcessCache;
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        if (messageText.equals("Так")) {
            changeStateToMatch(chatId);
            UserDto userDto = registrationProcessCache.remove(chatId);
            userService.saveUser(userDto);
            profileSender.showProfile(chatId);
            profileSender.sendNextProfile(chatId);
        } else if (messageText.equals("Змінити")){
            changeStateToName(chatId);
        } else {
            log.error("??");
        }
    }

    private void changeStateToName(Long chatId) {
        registrationProcessCache.getCurrentUser(chatId).setState(State.NAME);
        messageSender.sendTextMessage(chatId, ResponseTemplate.NAME_INPUT);
    }

    private void changeStateToMatch(Long chatId) {
        registrationProcessCache.getCurrentUser(chatId).setState(State.MATCH);
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
