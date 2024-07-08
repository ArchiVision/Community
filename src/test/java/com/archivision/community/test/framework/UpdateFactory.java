package com.archivision.community.test.framework;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class UpdateFactory {
    public static Update of(Long telegramUserId, String username, String text) {
        final User userFrom = new User();
        userFrom.setId(telegramUserId);
        userFrom.setUserName(username);

        final Message message = new Message();
        message.setFrom(userFrom);
        message.setText(text);

        final Update update = new Update();
        update.setMessage(message);

        return update;

    }
}
