package com.hunch.realestate.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PagingResult<T> {
    private final List<T> content;
    private final int currentPage;
    private final int pageSize;
    private final int totalCount;
    private final int totalPages;

    public boolean hasNext() {
        return currentPage < totalPages - 1;
    }

    public boolean hasPrevious() {
        return currentPage > 0;
    }

    public int getStartPage() {
        return Math.max(0, currentPage - 5);
    }

    public int getEndPage() {
        return Math.min(totalPages - 1, currentPage + 5);
    }
}