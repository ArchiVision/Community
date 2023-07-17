package com.archivision.community.receiver;

import com.archivision.community.processor.UpdateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramUpdateReceiver implements UpdateReceiver {
    private UpdateProcessor updateProcessor;

    public void receiveUpdate(Update update) {
        updateProcessor.processUpdate(update);
    }

    @Autowired
    public void setUpdateProcessor(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }
}
