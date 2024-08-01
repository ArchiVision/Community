package com.archivision.community.state.impl.initial;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.cache.ActiveViewingData;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.dto.UserDto;
import com.archivision.community.matcher.MatchedUsersListResolver;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.ProfileSender;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class ApproveStateHandler extends AbstractStateHandler {
    private final ProfileSender profileSender;
    private final MatchedUsersListResolver matchedUsersListResolver;
    private final RedisTemplate<Long, String> redisTemplate;
    private final ActiveViewingData activeViewingData;

    public ApproveStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                               KeyboardBuilderService keyboardBuilder, ProfileSender profileSender, MatchedUsersListResolver matchedUsersListResolver, RedisTemplate<Long, String> redisTemplate, ActiveViewingData activeViewingData,
                               UserCache userCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, userCache);
        this.profileSender = profileSender;
        this.matchedUsersListResolver = matchedUsersListResolver;
        this.redisTemplate = redisTemplate;
        this.activeViewingData = activeViewingData;
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        if (messageText.equals("Так")) {
            UserDto userDto = userCache.remove(chatId);
            userService.saveUser(userDto);
            userService.changeState(chatId, UserFlowState.MATCH);
        } else if (messageText.equals("Змінити")){
            userService.changeState(chatId, UserFlowState.NAME);
        } else {
            log.error("??");
        }
    }

    @Override
    public UserFlowState getState() {
        return UserFlowState.APPROVE;
    }

    @Override
    public boolean shouldValidateInput() {
        return false;
    }

    @Override
    public void onStateChanged(Long chatId) {
        messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.APPROVE_INPUT, keyboardBuilder.approvalButtons());
    }
}
