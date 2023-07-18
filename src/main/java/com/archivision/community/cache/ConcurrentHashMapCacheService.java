package com.archivision.community.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ConcurrentHashMapCacheService<K, V> implements CacheService<K, V> {
    protected final Map<K, V> cache = new ConcurrentHashMap<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public V remove(K key) {
        return cache.remove(key);
    }
}
