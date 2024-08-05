package com.archivision.community.repo;

import com.archivision.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramUserId(Long id);
    void deleteByTelegramUserId(Long id);
    List<User> findAllByTelegramUserIdNot(Long id);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.topics WHERE u.telegramUserId = :chatId")
    Optional<User> findByIdWithTopics(@Param("chatId") Long chatId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.numberOfViews = u.numberOfViews + 1 WHERE u.telegramUserId = :chatId")
    void incrementNumberOfViews(@Param("chatId") Long chatId);
}
