package com.archivision.community.service;

import com.archivision.community.entity.UserLike;
import com.archivision.community.event.Like;
import com.archivision.community.repo.UserLikeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserLikeService {
    private final UserLikeRepo userLikeRepo;
    final private RabbitTemplate rabbitTemplate;

    public void like(Long userId, Long userLikeId) {
        UserLike userLike = new UserLike();
        userLike.setUserId(userId);
        userLike.setLikedUserId(userLikeId);
        userLikeRepo.save(userLike);
        Like likeEvent = new Like(userId, userLikeId);
        rabbitTemplate.convertAndSend("like-events", likeEvent);
    }

    public boolean existsReverseLike(Long likerId, Long likedId) {
        long count = userLikeRepo.countByUserIdAndLikedUserId(likedId, likerId);
        return count > 0;
    }
}
