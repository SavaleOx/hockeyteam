package com.savaleox.hockeyteam.cache;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SearchCacheManager {
    private final ConcurrentHashMap<SearchCacheKey, Page<?>> cache = new ConcurrentHashMap<>();

    public <T> Page<T> get(SearchCacheKey key) {
        return (Page<T>) cache.get(key);
    }

    public <T> void put(SearchCacheKey key, Page<T> page) {
        cache.put(key, page);
    }

    public void invalidateAll() {
        cache.clear();
    }
}