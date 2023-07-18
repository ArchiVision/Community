package com.archivision.community.cache;

import com.archivision.community.dto.UserDto;
import com.archivision.community.exception.MissingTelegramIdException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * May be useful in future when there will be troubles with performance.
 * Ideally, it's better to use Redis/Hazelcast/EhCache.
 */
@Component
public class ActiveUserFillingDataCache extends ConcurrentHashMapCacheService<Long, UserDto> {
    public void put(UserDto userDto) {
        Long telegramId = Optional.ofNullable(userDto.getTelegramUserId()).orElseThrow(() ->
                new MissingTelegramIdException("UserDto has no telegram id to be putted in cache"));
        cache.put(telegramId, userDto);
    }
}
