package com.archivision.community.service;

import com.archivision.community.dto.UserDto;

import java.util.Optional;
import java.util.function.Consumer;

public interface UserCache {

    void add(Long chatId, UserDto userDto);

    UserDto get(Long chatId);

    UserDto remove(Long chatId);

    void processUser(Long chatId, Consumer<UserDto> dtoConsumer);
}
