package com.archivision.community.service.user;

import com.archivision.community.entity.Topic;
import com.archivision.community.entity.User;
import com.archivision.community.repo.TopicRepository;
import com.archivision.community.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserTopicService {
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public UserTopicService(UserRepository userRepository, TopicRepository topicRepository) {
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public void addTopicToUser(Long telegramId, String topicName) {
        User user = userRepository.findByTelegramUserId(telegramId).orElse(null);
        Topic topic = topicRepository.findByName(topicName).orElse(null);

        if (topic == null) {
            Topic savedTopic = new Topic();
            savedTopic.setName(topicName);
            topic = topicRepository.save(savedTopic);
        }

        if (user != null) {
            user.getTopics().add(topic);
        }
    }

    @Transactional
    public void removeTopicFromUser(Long telegramId, Long topicId) {
        User user = userRepository.findByTelegramUserId(telegramId).orElse(null);
        Topic topic = topicRepository.findById(topicId).orElse(null);

        if (user != null && topic != null) {
            user.getTopics().remove(topic);
        }
    }

    @Transactional
    public void createTopic(String topicName) {
        Topic topic = new Topic();
        topic.setName(topicName);
        topicRepository.save(topic);
    }

    @Transactional
    public void removeTopic(Long topicId) {
        topicRepository.findById(topicId).ifPresent(this::removeTopicFromAllUsers);
    }

    private void removeTopicFromAllUsers(Topic topic) {
        for (User user : topic.getUsers()) {
            user.getTopics().remove(topic);
        }
        topicRepository.delete(topic);
    }
}

