package com.archivision.community.cache;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InputTopicsCache extends ConcurrentHashMapCacheService<Long, List<String>> {
}
