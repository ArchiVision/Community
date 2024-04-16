package com.archivision.bot.state.impl.initial;

import com.archivision.bot.cache.ActiveRegistrationProcessCache;
import com.archivision.bot.command.ResponseTemplate;
import com.archivision.bot.sender.MessageSender;
import com.archivision.bot.service.KeyboardBuilderService;
import com.archivision.bot.service.user.UserService;
import com.archivision.bot.state.AbstractStateHandler;
import com.archivision.bot.util.InputValidator;
import com.archivision.common.model.bot.UserFlowState;
import com.archivision.common.model.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.common.model.bot.UserFlowState.AGE;

@Component
@Slf4j
public class NameInputStateHandler extends AbstractStateHandler {
    private static final String ERROR_NAME = "Щось не так з ім'ям. Спробуй ще раз";

    public NameInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                 KeyboardBuilderService keyboardBuilder, ActiveRegistrationProcessCache registrationProcessCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, registrationProcessCache);
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        UserDto user = registrationProcessCache.getCurrentUser(chatId);
        String messageText = message.getText();
        user.setUserFlowState(AGE);
        user.setName(messageText);
        messageSender.sendTextMessage(chatId, ResponseTemplate.AGE_INPUT);
    }

    @Override
    public void onValidationError(Message message) {
        messageSender.sendTextMessage(message.getChatId(), ERROR_NAME);
    }

    @Override
    public boolean isInputValid(Message message) {
        return inputValidator.isNameValid(message.getText());
    }

    @Override
    public UserFlowState getState() {
        return UserFlowState.NAME;
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }
}
