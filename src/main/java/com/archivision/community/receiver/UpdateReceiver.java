package com.archivision.community.receiver;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateReceiver {
    void receiveUpdate(Update update);
}
