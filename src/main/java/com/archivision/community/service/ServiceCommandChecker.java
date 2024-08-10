package com.archivision.community.service;

import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.model.FilterResult;
import com.archivision.community.model.Subscription;
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
        if (commands.contains(message.getText())) {
            final List<Subscription> subscriptionTypes = subscriptionService.getAvailableSubscriptionTypes();
            messageSender.sendTextMessage(message.getChatId(), formMessage(subscriptionTypes));
        }
        return FilterResult.builder().processNext(true).message("Success").build();
    }

    private String formMessage(List<Subscription> subscriptionTypes) {
        return subscriptionTypes.stream()
                .map(Subscription::getName)
                .collect(Collectors.joining(", "));
    }
}
