package com.savaleox.hockeyteam.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SearchCacheManager {
    private final ConcurrentHashMap<SearchCacheKey, Page<?>> cache = new ConcurrentHashMap<>();

    public <T> Page<T> get(SearchCacheKey key) {
        Page<T> result = (Page<T>) cache.get(key);
        System.out.print("check");
        if (result != null) {
            System.out.print("cache hit");
            log.info("CACHE HIT: searchWithFilters for key {}", key);
        } else {
            System.out.print("cache miss");
            log.info("CACHE MISS: searchWithFilters for key {}", key);
        }
        return result;
    }

    public <T> void put(SearchCacheKey key, Page<T> page) {
        cache.put(key, page);
        log.info("CACHE PUT: key={}, totalElements={}, totalPages={}",
                key, page.getTotalElements(), page.getTotalPages());
    }

    public void invalidateAll() {
        int size = cache.size();
        cache.clear();
        log.info("CACHE INVALIDATED: removed {} entries", size);
    }
}