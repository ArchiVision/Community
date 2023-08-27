package com.archivision.community.state;

import com.archivision.community.cache.ActiveRegistrationProcessCache;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.util.InputValidator;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public abstract class AbstractStateHandler implements StateHandler, Validatable {
    protected final InputValidator inputValidator;
    protected final UserService userService;
    protected final MessageSender messageSender;
    protected final KeyboardBuilderService keyboardBuilder;
    protected final ActiveRegistrationProcessCache registrationProcessCache;

    public void handle(Message message) {
        if (shouldValidateInput()) {
            if (!isInputValid(message)) {
                onValidationError(message);
                return;
            }
        }
        doHandle(message);
    }
    public abstract void doHandle(Message message);
    public void onValidationError(Message message) {
        // default impl
    }
    public boolean isInputValid(Message message) {
        // default impl
        return false;
    }
}
