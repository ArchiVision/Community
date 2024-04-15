package com.archivision.community.state.impl;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.cache.ActiveRegistrationProcessCache;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.SubscriptionService;
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
    public SettingsStateHandler(InputValidator inputValidator, UserService userService, MessageSender messageSender, KeyboardBuilderService keyboardBuilder, ActiveRegistrationProcessCache registrationProcessCache, SubscriptionService subscriptionService) {
        super(inputValidator, userService, messageSender, keyboardBuilder, registrationProcessCache);
        this.subscriptionService = subscriptionService;
    }

    private final SubscriptionService subscriptionService;

    @Override
    public void doHandle(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        log.info("message on settings state");
        formMarkupBasedOnDonationType(text, chatId);
    }

    private void formMarkupBasedOnDonationType(String subscriptionType, Long chatId) {
        InlineKeyboardMarkup markup = keyboardBuilder.inlineBtn(subscriptionType, getPaymentUrl(chatId, subscriptionType));
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

    }
}
