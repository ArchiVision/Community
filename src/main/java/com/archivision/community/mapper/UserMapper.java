package com.archivision.community.mapper;

import com.archivision.community.dto.UserDto;
import com.archivision.community.entity.Topic;
import com.archivision.community.entity.User;
import com.archivision.community.repo.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserMapper {
    private final TopicMapper topicMapper;
    private final TopicRepository topicRepository;


    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId() != null ? Long.valueOf(userDto.getId()) : null);
        user.setTelegramUserId(userDto.getTelegramUserId());
        user.setName(userDto.getName());
        user.setCity(userDto.getCity());
        user.setDescription(userDto.getDescription());
        user.setAge(userDto.getAge());
        user.setUserFlowState(userDto.getUserFlowState());
        user.setUsername(userDto.getUsername());
        user.setGender(userDto.getGender());
        user.setLookingFor(userDto.getLookingFor());
        user.setPhotoId(userDto.getPhotoId());

        Set<Topic> attachedTopics = new HashSet<>();
        userDto.getTopics().forEach(topicDto -> {
            Optional<Topic> existingTopic = topicRepository.findByName(topicDto.getName());
            if (existingTopic.isPresent()) {
                attachedTopics.add(existingTopic.get());
            } else {
                Topic newTopic = topicMapper.toEntity(topicDto);
                attachedTopics.add(newTopic);
            }
        });

        user.setTopics(attachedTopics);
        return user;
    }
}

