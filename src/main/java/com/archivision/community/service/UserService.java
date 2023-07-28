package com.archivision.community.service;

import com.archivision.community.bot.State;
import com.archivision.community.entity.Topic;
import com.archivision.community.entity.User;
import com.archivision.community.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void changeState(Long userId, State userState) {
        Optional<User> byId = userRepository.findByTelegramUserId(userId);
        byId.ifPresentOrElse(user -> {
            user.setState(userState);
        }, () -> log.info("User with id={} not found", userId));
    }

    public Optional<User> getUserByTelegramId(Long chatId) {
        return userRepository.findByTelegramUserId(chatId);
    }

    @Transactional
    public User getUserByTgId(Long chatId) {
        User user = userRepository.findByTelegramUserId(chatId).get();
        user.getTopics().contains("1");
        return user;
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public void deleteByTgId(Long id) {
        userRepository.deleteByTelegramUserId(id);
    }

    public List<User> findAllExceptId(Long id) {
        return userRepository.findAllByTelegramUserIdNot(id);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

