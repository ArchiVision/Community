package com.archivision.community.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.archivision.community.model.Reply.*;

@Component
public class KeyboardBuilderService {

    public ReplyKeyboardMarkup generateSkipButton() {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(Collections.singleton(KeyboardButton.builder()
                        .text(SKIP.toString())
                        .build())))
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup generateGenderButtons() {
        return generateMultiButtons(MAN.toString(), GIRL.toString(), OTHER.toString());
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
        return generateMultiButtons(YES.toString(), CHANGE.toString());
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
