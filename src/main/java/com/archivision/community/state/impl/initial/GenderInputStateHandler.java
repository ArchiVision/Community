package com.archivision.community.state.impl.initial;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.cache.ActiveRegistrationProcessCache;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.dto.UserDto;
import com.archivision.community.entity.Gender;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.user.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Set;

@Component
@Slf4j
public class GenderInputStateHandler extends AbstractStateHandler  {
    public GenderInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                   KeyboardBuilderService keyboardBuilder, ActiveRegistrationProcessCache registrationProcessCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, registrationProcessCache);
    }

    private static final Set<String> options = Set.of("Хлопець", "Дівчина", "Інше");

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        UserDto user = registrationProcessCache.getCurrentUser(chatId);
        String messageText = message.getText();
        user.setUserFlowState(UserFlowState.LOOKING);
        user.setGender(Gender.fromString(messageText));
        messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.LOOKING_FOR_INPUT,
                keyboardBuilder.lookingGenderButtons());
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Smth went wrong. Message(gender)={}", message.getText());
    }

    @Override
    public boolean isInputValid(Message message) {
        return options.contains(message.getText());
    }

    @Override
    public UserFlowState getState() {
        return UserFlowState.GENDER;
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }
}
