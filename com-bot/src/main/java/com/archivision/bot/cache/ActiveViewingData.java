package com.archivision.bot.cache;

import org.springframework.stereotype.Component;

@Component
public class ActiveViewingData extends ConcurrentHashMapCacheService<Long, Long> {

}
