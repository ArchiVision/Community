package com.archivision.community.repo;

import com.archivision.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramUserId(Long id);
    void deleteByTelegramUserId(Long id);
    List<User> findAllByTelegramUserIdNot(Long id);
}
