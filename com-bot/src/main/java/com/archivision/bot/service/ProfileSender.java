package com.archivision.bot.service;


import com.archivision.bot.cache.ActiveViewingData;
import com.archivision.bot.sender.MessageSender;
import com.archivision.bot.service.user.UserService;
import com.archivision.common.model.entity.Topic;
import com.archivision.common.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileSender {
    private final UserService userService;
    private final TelegramImageS3Service telegramImageS3Service;
    private final MessageSender messageSender;
    private final ActiveViewingData activeViewingData;

    public void showUserProfileTo(Long chatId, Long userTo) {
        User user = userService.getUserByTgIdWithTopics(chatId);
        String formattedProfileText = getFormattedProfileText(user);
        if (Objects.equals(chatId, userTo)) {
            messageSender.sendTextMessage(userTo, "Твоя анкета:");
        }
        boolean hasPhoto = user.getPhotoId() != null;
        telegramImageS3Service.sendImageOfUserToUser(chatId, userTo, hasPhoto, formattedProfileText);
        log.info("showing profile");
    }

    public void sendNextProfile(Long chatId) {
        giveUserPersonList(chatId)
                .ifPresentOrElse(user -> {
                    activeViewingData.put(chatId, user.getTelegramUserId());
                    showUserProfileTo(user.getTelegramUserId(), chatId);
                }, () -> messageSender.sendTextMessage(chatId, "Анкети закінчилися :("));
    }

    @SneakyThrows
    private Optional<User> giveUserPersonList(Long chatId) {
        List<User> allUsers = userService.findAllExceptId(chatId);
        return allUsers.size() > 0 ? Optional.of(allUsers.get(0)) : Optional.empty();
    }

    public void showProfile(Long selfChatId) {
        showUserProfileTo(selfChatId, selfChatId);
    }

    private String getFormattedProfileText(User user) {
        String formattedProfileText;
        formattedProfileText = """
            %s, %s, %s
                        
            Теми: %s
                        
            Опис: %s
            """.formatted(user.getName(), user.getAge(), user.getCity(), formatTopics(user.getTopics()), user.getDescription() == null ? "*пусто*" : user.getDescription());
        return formattedProfileText;
    }

    private String formatTopics(Set<Topic> topics) {
        return topics.isEmpty() ? "*відсутні*" :
                topics.stream().map(Topic::getName).collect(Collectors.joining(", "));
    }
}
