package com.archivision.community.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
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

    public ReplyKeyboardMarkup generateGenderButtons() {
        return generateMultiButtons("Хлопець", "Дівчина", "Інше");
    }

    public ReplyKeyboardMarkup generateLookingGenderButtons() {
        return generateMultiButtons("Хлопців", "Дівчат", "Все одно");
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
        return generateMultiButtons(YES, CHANGE);
    }

    private KeyboardButton buttonWith(String text) {
        return KeyboardButton.builder()
                .text(text)
                .build();
    }

    public ReplyKeyboardMarkup generateMultiButtons(String ... buttonTexts) {
        List<KeyboardButton> buttonList = Arrays.stream(buttonTexts)
                .map(this::buttonWith)
                .toList();
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(buttonList))
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();
    }
}
