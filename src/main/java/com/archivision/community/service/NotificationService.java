package com.archivision.community.service;

import com.archivision.community.event.LikeEvent;
import com.archivision.community.messagesender.MessageSender;
import com.archivision.community.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final MessageSender messageSender;
    private final UserService userService;
    private final ProfileSender profileSender;

    private final static String SYMPATHY = "У вас симпатія! @";

    public void notifyUsersAboutMatch(Long likerId, Long likedId) {
        messageSender.sendTextMessage(likedId, SYMPATHY +
                userService.getUserByTgId(likerId).getUsername());
        profileSender.showUserProfileTo(likerId, likedId);

        messageSender.sendTextMessage(likerId, SYMPATHY +
                userService.getUserByTgId(likedId).getUsername());
        profileSender.showUserProfileTo(likedId, likerId);
    }

    public void notifyUserAboutSuccessfulPayment(String chatId, String message) {
        messageSender.sendTextMessage(Long.valueOf(chatId), message);
    }

    public void notifyPersonAboutLike(LikeEvent likeEvent) {
        final Long whoIsLiked = likeEvent.liked();

        messageSender.sendTextMessage(whoIsLiked, "У вас вподобання!");
        profileSender.showUserProfileTo(likeEvent.liker(), whoIsLiked);
    }
}
