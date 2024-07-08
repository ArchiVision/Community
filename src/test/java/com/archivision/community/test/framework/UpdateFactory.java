package com.archivision.community.test.framework;

import org.checkerframework.checker.units.qual.C;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class UpdateFactory {
    public static Update of(Long telegramUserId, String username, String text) {
        final User userFrom = new User();
        userFrom.setId(telegramUserId);
        userFrom.setUserName(username);

        final Chat chat = new Chat();
        chat.setId(1L);

        final Message message = new Message();
        message.setFrom(userFrom);
        message.setText(text);
        message.setChat(chat);

        final Update update = new Update();
        update.setMessage(message);

        return update;
    }
}
