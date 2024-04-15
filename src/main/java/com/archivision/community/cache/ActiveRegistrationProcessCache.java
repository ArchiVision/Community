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
public class ActiveRegistrationProcessCache extends ConcurrentHashMapCacheService<Long, UserDto> {
    public void put(UserDto userDto) {
        cache.put(Optional.ofNullable(userDto.getTelegramUserId()).orElseThrow(() ->
                new MissingTelegramIdException("UserDto has no telegram id to be putted in cache")), userDto);
    }

    public UserDto getCurrentUser(Long tgId) {
        return get(tgId).orElseThrow(() -> new RuntimeException("No user in cache with id=" + tgId));
    }
}
