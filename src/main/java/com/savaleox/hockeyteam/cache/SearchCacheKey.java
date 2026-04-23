package com.savaleox.hockeyteam.cache;

import com.savaleox.hockeyteam.dto.PlayerSearchCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Objects;

public final class SearchCacheKey {
    private final PlayerSearchCriteria criteria;
    private final int page;
    private final int size;
    private final String sort;

    public SearchCacheKey(PlayerSearchCriteria criteria, Pageable pageable) {
        this.criteria = criteria;
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
        this.sort = pageable.getSort().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchCacheKey that)) return false;
        return page == that.page && size == that.size &&
                Objects.equals(criteria, that.criteria) &&
                Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(criteria, page, size, sort);
    }
}