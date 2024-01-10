package com.archivision.community.service;

import com.archivision.community.cache.ActiveViewingData;
import com.archivision.community.entity.Topic;
import com.archivision.community.entity.User;
import com.archivision.community.matcher.MatchedUsersListResolver;
import com.archivision.community.matcher.model.UserWithMatchedProbability;
import com.archivision.community.messagesender.MessageSender;
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
    private final MatchedUsersListResolver matchedUsersListResolver;
    private final ActiveViewingData activeViewingData;

    public void showUserProfileTo(Long chatId, Long userTo) {
        User user = userService.getUserByTgIdWithTopics(chatId);
        String formattedProfileText = getFormattedProfileText(user);
        if (Objects.equals(chatId, userTo)) {
            messageSender.sendTextMessage(userTo, "Твоя анкета:");
        }
        boolean hasPhoto = !(user.getPhotoId() == null);
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
        User user = userService.getUserByTgIdWithTopics(chatId);
        List<User> allUsers = userService.findAllExceptId(chatId);
//        List<User> orderedMatchingList = matchedUsersListResolver.getOrderedMatchingList(user, allUsers).stream()
//                .map(UserWithMatchedProbability::user)
//                .toList();
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
