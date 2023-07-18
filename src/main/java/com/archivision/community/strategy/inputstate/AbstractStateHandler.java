package com.archivision.community.strategy.inputstate;

import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.KeyboardBuilderService;
import com.archivision.community.service.UserService;
import com.archivision.community.util.InputValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractStateHandler implements StateHandler {
    protected final InputValidator inputValidator;
    protected final UserService userService;
    protected final MessageSender messageSender;
    protected final KeyboardBuilderService keyboardBuilder;
}
