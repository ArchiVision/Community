package com.archivision.bot.service;


import com.archivision.bot.model.FilterResult;
import com.archivision.bot.model.Subscription;
import com.archivision.bot.sender.MessageSender;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceCommandChecker {
    private final Set<String> commands = new HashSet<>();
    private final MessageSender messageSender;
    private final SubscriptionService subscriptionService;

    @PostConstruct
    public void init() {
        commands.add("/subscriptions");
    }

    public FilterResult filter(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        if (commands.contains(text)) {
            List<Subscription> subscriptionTypes = subscriptionService.getAvailableSubscriptionTypes();
            messageSender.sendTextMessage(chatId, formMessage(subscriptionTypes));
        }
        return FilterResult.builder().processNext(true).message("Success").build();
    }

    private String formMessage(List<Subscription> subscriptionTypes) {
        return subscriptionTypes.stream()
                .map(Subscription::getName)
                .collect(Collectors.joining(", "));
    }
}
