package com.archivision.bot.state.impl;


import com.archivision.bot.cache.ActiveRegistrationProcessCache;
import com.archivision.bot.sender.MessageSender;
import com.archivision.bot.service.KeyboardBuilderService;
import com.archivision.bot.service.SubscriptionService;
import com.archivision.bot.service.user.UserService;
import com.archivision.bot.state.AbstractStateHandler;
import com.archivision.bot.state.Validatable;
import com.archivision.bot.util.InputValidator;
import com.archivision.common.model.bot.UserFlowState;
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
