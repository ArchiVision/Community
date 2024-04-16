package com.archivision.bot.repository;

import com.archivision.common.model.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserLikeRepo extends JpaRepository<UserLike, Long> {

    @Query(nativeQuery = true, value = QUERY)
    long countByUserIdAndLikedUserId(Long userId, Long likedUserId);

    String QUERY = """
            SELECT COUNT(*)\s
            FROM user_like\s
            WHERE user_id = :userId AND liked_user_id = :likedUserId
            """;
}
