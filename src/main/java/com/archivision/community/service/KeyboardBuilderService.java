package com.archivision.community.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;
import java.util.List;

@Component
public class KeyboardBuilderService {

    private static final String SKIP = "Пропустити";

    public ReplyKeyboardMarkup generateSkipButton() {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(Collections.singleton(KeyboardButton.builder()
                        .text(SKIP)
                        .build())))
                .build();
    }

    public ReplyKeyboardMarkup generateApprovalButtons() {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(
                        KeyboardButton.builder()
                                .text("Так")
                                .build(),
                        KeyboardButton.builder()
                                .text("Змінити")
                                .build()
                )))
                .oneTimeKeyboard(true)
                .build();
    }
}
