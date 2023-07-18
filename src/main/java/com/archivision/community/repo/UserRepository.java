package com.archivision.community.repo;

import com.archivision.community.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByTelegramUserId(Long id);
}
