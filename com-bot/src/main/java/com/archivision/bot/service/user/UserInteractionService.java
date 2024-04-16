package com.archivision.bot.service.user;

import com.archivision.bot.cache.ActiveViewingData;
import com.archivision.bot.service.ProfileSender;
import com.archivision.matcher.service.MatchedUsersListResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserInteractionService {

    private final MatchedUsersListResolver matchedUsersListResolver;
    private final ActiveViewingData activeViewingData;
    private final UserLikeService userLikeService;
    private final ProfileSender profileSender;

    public UserInteractionService(MatchedUsersListResolver matchedUsersListResolver, ActiveViewingData activeViewingData,
                                  UserLikeService userLikeService, ProfileSender profileSender) {
        this.matchedUsersListResolver = matchedUsersListResolver;
        this.activeViewingData = activeViewingData;
        this.userLikeService = userLikeService;
        this.profileSender = profileSender;
    }

    public void handleLikeAction(Long chatId) {
        log.info("Like");
        Optional<Long> viewingUser = activeViewingData.get(chatId);
        viewingUser.ifPresent(checkingThisUser -> {
            activeViewingData.remove(chatId);
            userLikeService.like(chatId, checkingThisUser);
        });
        profileSender.sendNextProfile(chatId);
    }

    public void handleDislikeAction(Long chatId) {
        log.info("Dislike");
        // logic for dislike action
    }
}

