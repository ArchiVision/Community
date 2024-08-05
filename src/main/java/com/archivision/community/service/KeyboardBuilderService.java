package com.archivision.community.service;

import com.archivision.community.model.Reply;
import com.archivision.community.model.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.archivision.community.model.Reply.*;

@Component
@RequiredArgsConstructor
public class KeyboardBuilderService {
    private final SubscriptionService subscriptionService;

    public ReplyKeyboardMarkup skipButton() {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(Collections.singleton(KeyboardButton.builder()
                        .text(SKIP.toString())
                        .build())))
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup backButton() {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(Collections.singleton(KeyboardButton.builder()
                        .text(BACK.toString())
                        .build())))
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup genderButtons() {
        return multiButtons(true, MAN.toString(), GIRL.toString(), OTHER.toString());
    }

    public ReplyKeyboardMarkup lookingGenderButtons() {
        return multiButtons(true, "Хлопців", "Дівчат", "Все одно");
    }

    public ReplyKeyboardMarkup button(String text) {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(){{
                    add(text);
                }})
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup approvalButtons() {
        return multiButtons(true, YES.toString(), CHANGE.toString());
    }

    private KeyboardButton buttonWith(String text) {
        return KeyboardButton.builder()
                .text(text)
                .build();
    }

    public ReplyKeyboardMarkup multiButtons(boolean oneTime, String ... buttonTexts) {
        List<KeyboardButton> buttonList = Arrays.stream(buttonTexts)
                .map(this::buttonWith)
                .toList();
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(buttonList))
                .oneTimeKeyboard(oneTime)
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup matchButtons() {
        return multiButtons(false, "+", "-", STATS.toString(), Reply.SETTINGS.toString());
    }

    public ReplyKeyboardMarkup subscriptions() {
        List<String> strings = subscriptionService.getAvailableSubscriptionTypes().stream().map(Subscription::getName).toList();
        return multiButtons(false, strings.toArray(new String[0]));
    }

    public InlineKeyboardMarkup inlineBtn(String subscription, String paymentUrl) {
        InlineKeyboardButton build = InlineKeyboardButton.builder().text(subscription).url(paymentUrl).build();
        return new InlineKeyboardMarkup(List.of(List.of(build)));
    }
}
