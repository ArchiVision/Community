package com.archivision.community.service.user;

import com.archivision.community.entity.User;
import com.archivision.community.repo.UserLikeRepo;
import com.archivision.community.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatsService {
    private final UserRepository userRepository;
    private final UserLikeRepo userLikeRepo;

    public double resolveUserPopularityIndex(Long chatId) {
        final User user = userRepository.findByTelegramUserId(chatId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        final long numberOfLikes = userLikeRepo.countNumberOfLikesForUser(user.getTelegramUserId());
        final long numberOfViews = user.getNumberOfViews();

        log.info("Likes: {}, views: {}", numberOfLikes, numberOfViews);
        if (numberOfViews == 0) {
            return 0.0;
        }
        return (double) numberOfLikes / numberOfViews;
    }
}