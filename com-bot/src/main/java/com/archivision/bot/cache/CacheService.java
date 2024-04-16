package com.archivision.bot.cache;

import java.util.Optional;

public interface CacheService<K, V> {
    void put(K key, V value);
    Optional<V> get(K key);
    V remove(K key);
}
