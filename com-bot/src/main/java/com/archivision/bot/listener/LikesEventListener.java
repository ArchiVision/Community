package com.archivision.bot.listener;

import com.archivision.bot.model.LikeEvent;
import com.archivision.bot.service.NotificationService;
import com.archivision.bot.service.user.UserLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikesEventListener {
    private final UserLikeService userLikeService;
    private final NotificationService notificationService;

    @RabbitListener(queues = "like-events")
    public void handleLike(LikeEvent likeEvent) {
        if (userLikeService.isReverseLikeExists(likeEvent.liker(), likeEvent.liked())) {
            notificationService.notifyUsersAboutMatch(likeEvent.liker(), likeEvent.liked());
        }
    }
}
