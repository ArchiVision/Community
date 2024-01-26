package com.archivision.community.service;

import com.archivision.community.bot.State;
import com.archivision.community.dto.UserDto;
import com.archivision.community.entity.User;
import com.archivision.community.mapper.UserMapper;
import com.archivision.community.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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

    public User getUserByTgId(Long chatId) {
        return userRepository.findByTelegramUserId(chatId).orElseThrow();
        // TODO: 03.09.2023 custom exception
    }

    public User getUserByTgIdWithTopics(Long chatId) {
        return userRepository.findByIdWithTopics(chatId).orElseThrow();
        // TODO: 03.09.2023 custom exception
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

    @Transactional
    public User saveUser(UserDto userDto) {
        return userRepository.save(userMapper.toEntity(userDto));
    }

    @Transactional
    public void changeSubscription(String chatId, User.Subscription subscription) {
        Optional<User> byId = userRepository.findByTelegramUserId(Long.valueOf(chatId));
        byId.ifPresentOrElse(user -> {
            user.setSubscription(subscription);
        }, () -> log.info("User with id={} not found", chatId));
    }
}

