package com.archivision.community.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;
import java.util.List;

@Component
public class KeyboardBuilderService {

    private static final String SKIP = "Пропустити";
    private static final String YES = "Так";
    private static final String CHANGE = "Змінити";

    public ReplyKeyboardMarkup generateSkipButton() {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(Collections.singleton(KeyboardButton.builder()
                        .text(SKIP)
                        .build())))
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup buildButtonWithText(String text) {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(){{
                    add(text);
                }})
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup generateApprovalButtons() {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(
                        buttonWith(YES),
                        buttonWith(CHANGE)
                )))
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();
    }

    private static KeyboardButton buttonWith(String text) {
        return KeyboardButton.builder()
                .text(text)
                .build();
    }
}
