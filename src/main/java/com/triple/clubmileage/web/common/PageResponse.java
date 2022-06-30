package com.triple.clubmileage.web.common;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter @Setter
public class PageResponse<T> {

    private int currentPage;
    private int totalPage;
    private int perPage;
    private long totalElements;
    private boolean hasPrevious;
    private boolean hasNext;
    private boolean isFirst;
    private boolean isLast;

    private List<T> contents;

    @Builder
    public PageResponse(int currentPage, int totalPage, int perPage, long totalElements, boolean hasPrevious, boolean hasNext, boolean isFirst, boolean isLast, List<T> contents) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.perPage = perPage;
        this.totalElements = totalElements;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.contents = contents;
    }

    public static<T> PageResponse<T> toPageResponse(Page page, List<T> contents) {
        return PageResponse.<T>builder()
                .currentPage(page.getNumber())
                .totalPage(page.getTotalPages())
                .perPage(page.getSize())
                .totalElements(page.getTotalElements())
                .hasPrevious(page.hasPrevious())
                .hasNext(page.hasNext())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .contents(contents)
                .build();
    }
}
