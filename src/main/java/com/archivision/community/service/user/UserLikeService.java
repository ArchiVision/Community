package com.archivision.community.service.user;

import com.archivision.community.entity.UserLike;
import com.archivision.community.event.LikeEvent;
import com.archivision.community.repo.UserLikeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserLikeService {
    private final UserLikeRepo userLikeRepo;
    private final RabbitTemplate rabbitTemplate;

    @Value("${community.likes-queue}")
    private String likesEventQueue;

    public void like(Long userId, Long userLikeId) {
        UserLike userLike = new UserLike();
        userLike.setUserId(userId);
        userLike.setLikedUserId(userLikeId);
        userLike.setDateTime(LocalDateTime.now());
        userLikeRepo.save(userLike);

        rabbitTemplate.convertAndSend(likesEventQueue, new LikeEvent(userId, userLikeId));
    }

    public boolean isReverseLikeExists(Long likerId, Long likedId) {
        return userLikeRepo.countByUserIdAndLikedUserId(likedId, likerId) > 0;
    }
}
