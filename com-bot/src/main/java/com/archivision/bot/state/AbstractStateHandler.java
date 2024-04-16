package com.archivision.bot.state;


import com.archivision.bot.cache.ActiveRegistrationProcessCache;
import com.archivision.bot.sender.MessageSender;
import com.archivision.bot.service.KeyboardBuilderService;
import com.archivision.bot.service.user.UserService;
import com.archivision.bot.util.InputValidator;
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
        if (shouldValidateInput() && (!isInputValid(message))) {
                onValidationError(message);
                return;
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
