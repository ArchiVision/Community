package com.archivision.bot.state.impl.initial;


import com.archivision.bot.cache.ActiveRegistrationProcessCache;
import com.archivision.bot.cache.ActiveViewingData;
import com.archivision.bot.command.ResponseTemplate;
import com.archivision.bot.sender.MessageSender;
import com.archivision.bot.service.KeyboardBuilderService;
import com.archivision.bot.service.ProfileSender;
import com.archivision.bot.service.user.UserService;
import com.archivision.bot.state.AbstractStateHandler;
import com.archivision.bot.util.InputValidator;
import com.archivision.common.model.bot.UserFlowState;
import com.archivision.common.model.dto.UserDto;
import com.archivision.matcher.service.MatchedUsersListResolver;
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
            messageSender.sendMsgWithMarkup(chatId, "Пошук", keyboardBuilder.matchButtons());
            profileSender.sendNextProfile(chatId);
        } else if (messageText.equals("Змінити")){
            changeStateToName(chatId);
        } else {
            log.error("??");
        }
    }

    private void changeStateToName(Long chatId) {
        registrationProcessCache.getCurrentUser(chatId).setUserFlowState(UserFlowState.NAME);
        messageSender.sendTextMessage(chatId, ResponseTemplate.NAME_INPUT);
    }

    private void changeStateToMatch(Long chatId) {
        registrationProcessCache.getCurrentUser(chatId).setUserFlowState(UserFlowState.MATCH);
    }

    @Override
    public UserFlowState getState() {
        return UserFlowState.APPROVE;
    }

    @Override
    public boolean shouldValidateInput() {
        return false;
    }
}
