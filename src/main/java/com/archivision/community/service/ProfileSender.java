package com.archivision.community.service;

import com.archivision.community.entity.Topic;
import com.archivision.community.entity.User;
import com.archivision.community.messagesender.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileSender {
    private final UserService userService;
    private final TelegramImageS3Service telegramImageS3Service;
    private final MessageSender messageSender;

    public void showProfileOfUserTo(Long chatId, Long userTo) {
        User user = userService.getUserByTgId(chatId);
        String formattedProfileText = getFormattedProfileText(user);
        if (Objects.equals(chatId, userTo)) {
            messageSender.sendTextMessage(userTo, "Твоя анкета:");
        }
        boolean hasPhoto = !(user.getPhotoId() == null);
        telegramImageS3Service.sendImageOfUserToUser(chatId, userTo, hasPhoto);
        messageSender.sendTextMessage(userTo, formattedProfileText);
        log.info("showing profile");
    }

    public void showProfile(Long selfChatId) {
        showProfileOfUserTo(selfChatId, selfChatId);
    }

    private String getFormattedProfileText(User user) {
        String formattedProfileText;
        formattedProfileText = """
            %s, %s, %s
                        
            Теми: %s
                        
            Опис: %s
            """.formatted(user.getName(), user.getAge(), user.getCity(), formatTopics(user.getTopics()), user.getDescription() == null ? "пусто" : user.getDescription());
        return formattedProfileText;
    }

    private String formatTopics(Set<Topic> topics) {
        return  "{" + topics.stream().map(Topic::getName).collect(Collectors.joining(",")) + "}";
    }
}
