package com.archivision.community.listener;

import com.archivision.community.event.LikeEvent;
import com.archivision.community.service.NotificationService;
import com.archivision.community.service.UserLikeService;
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
        boolean match = userLikeService.isReverseLikeExists(likeEvent.liker(), likeEvent.liked());
        if (match) {
            notificationService.notifyUsersAboutMatch(likeEvent.liker(), likeEvent.liked());
        }
    }
}
