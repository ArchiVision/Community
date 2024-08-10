package com.archivision.community.state.impl;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.SubscriptionService;
import com.archivision.community.service.UserCache;
import com.archivision.community.service.user.UserService;
import com.archivision.community.state.AbstractStateHandler;
import com.archivision.community.state.Validatable;
import com.archivision.community.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@Component
public class SettingsStateHandler extends AbstractStateHandler implements Validatable {
    private final SubscriptionService subscriptionService;

    public SettingsStateHandler(InputValidator inputValidator,
                                UserService userService,
                                MessageSender messageSender,
                                KeyboardBuilderService keyboardBuilder,
                                UserCache userCache,
                                SubscriptionService subscriptionService) {
        super(inputValidator, userService, messageSender, keyboardBuilder, userCache);
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void doHandle(Message message) {
        log.info("message on settings state");
        formMarkupBasedOnDonationType(message.getText(), message.getChatId());
    }

    private void formMarkupBasedOnDonationType(String subscriptionType, Long chatId) {
        final InlineKeyboardMarkup markup = keyboardBuilder.inlineBtn(subscriptionType, getPaymentUrl(chatId, subscriptionType));
        messageSender.sendMsgWithInline(chatId, markup);

    }

    private String getPaymentUrl(Long chatId, String subscriptionType) {
        return subscriptionService.getPaymentUrl(chatId, subscriptionType);
    }

    @Override
    public UserFlowState getState() {
        return UserFlowState.SETTINGS;
    }

    @Override
    public boolean shouldValidateInput() {
        return true;
    }

    @Override
    public boolean isInputValid(Message message) {
        return true;
    }

    @Override
    public void onValidationError(Message message) {
        log.error("Validation error in state: {}. Message={}", getState(), message.getText());
    }
}
