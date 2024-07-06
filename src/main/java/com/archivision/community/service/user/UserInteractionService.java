package com.archivision.community.service.user;

import com.archivision.community.cache.ActiveViewingData;
import com.archivision.community.matcher.MatchedUsersListResolver;
import com.archivision.community.service.ProfileSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInteractionService {

    private final ActiveViewingData activeViewingData;
    private final UserLikeService userLikeService;
    private final ProfileSender profileSender;

    public void handleLikeAction(Long chatId) {
        log.info("Like");

        activeViewingData.get(chatId).ifPresent(checkingThisUser -> {
            activeViewingData.remove(chatId);
            userLikeService.like(chatId, checkingThisUser);
        });

        profileSender.sendNextProfile(chatId);
    }

    public void handleDislikeAction(Long chatId) {
        log.info("Dislike");
        // TODO: logic for dislike action
    }
}

