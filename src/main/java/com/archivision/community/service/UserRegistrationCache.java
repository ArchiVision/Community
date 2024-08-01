package com.archivision.community.service;

import com.archivision.community.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Component
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationCache implements UserCache {

    private final RedisTemplate<Long, UserDto> redisTemplate;

    public void add(Long chatId, UserDto userDto) {
        redisTemplate.opsForValue().set(chatId, userDto);
    }

    // only read-only ops
    public UserDto get(Long chatId) {
        return redisTemplate.opsForValue().get(chatId);
    }

    public UserDto remove(Long chatId) {
        return redisTemplate.opsForValue().getAndDelete(chatId);
    }

    // use this
    public void processUser(Long chatId, Consumer<UserDto> dtoConsumer){
        UserDto userDto = get(chatId);
        if (userDto != null) {
            dtoConsumer.accept(userDto);
            add(chatId, userDto);
        }
    }
}
