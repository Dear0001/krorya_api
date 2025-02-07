package com.kshrd.kroya_api.payload.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationMeta {
    private long totalCategories;
    private int totalPages;
    private int currentPage;
    private int size;
    private String nextLink;
    private String prevLink;
}