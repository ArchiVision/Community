package com.archivision.community.state.impl.initial;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.command.ResponseTemplate;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.model.UserType;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.archivision.community.bot.UserFlowState.AGE;
import static com.archivision.community.bot.UserFlowState.LOOKING;

@Component
@Slf4j
public class NameInputStateHandler extends AbstractStateHandler {
    private static final String ERROR_NAME = "Щось не так з ім'ям. Спробуй ще раз";

    public NameInputStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender,
                                 KeyboardBuilderService keyboardBuilder, UserCache userCache) {
        super(inputValidator, userService, messageSender, keyboardBuilder, userCache);
    }

    @Override
    public void doHandle(Message message) {
        Long chatId = message.getChatId();
        String messageText = message.getText();
        userCache.processUser(chatId, userDto -> {
            userDto.setName(messageText);
            if (userDto.getUserType().equals(UserType.UNIT)) {
                userDto.setUserFlowState(LOOKING);
                messageSender.sendMsgWithMarkup(chatId, ResponseTemplate.LOOKING_FOR_INPUT, keyboardBuilder.lookingGenderButtons());
                return;
            }
            userDto.setUserFlowState(AGE);
            messageSender.sendTextMessage(chatId, ResponseTemplate.AGE_INPUT);
        });
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
