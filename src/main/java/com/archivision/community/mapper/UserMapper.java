package com.archivision.community.mapper;

import com.archivision.community.bot.State;
import com.archivision.community.dto.UserDto;
import com.archivision.community.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserMapper {
    private final TopicMapper topicMapper;
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId().toString());
        userDto.setTelegramUserId(user.getTelegramUserId());
        userDto.setName(user.getName());
        userDto.setCity(user.getCity());
        userDto.setDescription(user.getDescription());
        userDto.setAge(user.getAge());
        userDto.setState(user.getState());
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
        userDto.getTopics().forEach(topicDto -> user.getTopics().add(topicMapper.toEntity(topicDto)));
        return user;
    }
}

