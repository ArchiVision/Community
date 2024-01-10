package com.archivision.community.mapper;

import com.archivision.community.dto.UserDto;
import com.archivision.community.entity.Topic;
import com.archivision.community.entity.User;
import com.archivision.community.repo.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserMapper {
    private final TopicMapper topicMapper;
    private final TopicRepository topicRepository;

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId().toString());
        userDto.setTelegramUserId(user.getTelegramUserId());
        userDto.setName(user.getName());
        userDto.setCity(user.getCity());
        userDto.setDescription(user.getDescription());
        userDto.setAge(user.getAge());
        userDto.setState(user.getState());
        userDto.setUsername(user.getUsername());
        userDto.setGender(user.getGender());
        userDto.setLookingFor(user.getLookingFor());
        userDto.setPhotoId(userDto.getPhotoId());
        user.getTopics().forEach(topic -> userDto.getTopics().add(topicMapper.toDto(topic)));
        return userDto;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId() != null ? Long.valueOf(userDto.getId()) : null);
        user.setTelegramUserId(userDto.getTelegramUserId());
        user.setName(userDto.getName());
        user.setCity(userDto.getCity());
        user.setDescription(userDto.getDescription());
        user.setAge(userDto.getAge());
        user.setState(userDto.getState());
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

