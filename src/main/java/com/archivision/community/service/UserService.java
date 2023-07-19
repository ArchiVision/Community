package com.archivision.community.service;

import com.archivision.community.bot.State;
import com.archivision.community.document.Topic;
import com.archivision.community.document.User;
import com.archivision.community.repo.TopicRepository;
import com.archivision.community.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Topic addTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    public User addTopicToUser(String userId, String topicId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found"));
        user.getTopics().add(topic);
        return userRepository.save(user);
    }

    public User removeTopicFromUser(String userId, String topicId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found"));
        user.getTopics().remove(topic);
        return userRepository.save(user);
    }

    public void changeState(Long userId, State userState) {
        Optional<User> byId = userRepository.findByTelegramUserId(userId);
        byId.ifPresentOrElse(user -> {
            user.setState(userState);
            userRepository.save(user);
        }, () -> log.info("User with id={} not found", userId));

    }
    public Optional<User> getUserByTelegramId(Long chatId) {
        return userRepository.findByTelegramUserId(chatId);
    }

    public User getUserByTgId(Long chatId) {
        return userRepository.findByTelegramUserId(chatId).get();
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    public void deleteByTgId(Long id) {
        userRepository.deleteByTelegramUserId(id);
    }

    public List<User> findAllExceptId(Long id) {
        return userRepository.findAllByTelegramUserIdNot(id);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
}

