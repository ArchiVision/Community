package com.archivision.community.repo;

import com.archivision.community.entity.UserLike;
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

    @Query(nativeQuery = true, value = QUERY_LIKES)
    long countNumberOfLikesForUser(long likedUserId);
    String QUERY_LIKES = """
            SELECT COUNT(*)\s
            FROM user_like\s
            WHERE liked_user_id = :likedUserId
            """;
}
