package com.archivision.bot.service.user;


import com.archivision.bot.model.LikeEvent;
import com.archivision.bot.repository.UserLikeRepo;
import com.archivision.common.model.entity.UserLike;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        userLikeRepo.save(userLike);
        rabbitTemplate.convertAndSend(likesEventQueue, new LikeEvent(userId, userLikeId));
    }

    public boolean isReverseLikeExists(Long likerId, Long likedId) {
        return userLikeRepo.countByUserIdAndLikedUserId(likedId, likerId) > 0;
    }
}
