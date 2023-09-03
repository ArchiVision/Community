package com.archivision.community.repo;

import com.archivision.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramUserId(Long id);
    void deleteByTelegramUserId(Long id);
    List<User> findAllByTelegramUserIdNot(Long id);
    @Query("SELECT u FROM User u JOIN FETCH u.topics WHERE u.telegramUserId = :chatId")
    Optional<User> findByIdWithTopics(@Param("chatId") Long chatId);
}
