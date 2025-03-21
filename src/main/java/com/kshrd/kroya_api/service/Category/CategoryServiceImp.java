package com.kshrd.kroya_api.service.Category;

import com.kshrd.kroya_api.entity.CategoryEntity;
import com.kshrd.kroya_api.exception.DuplicateFieldExceptionHandler;
import com.kshrd.kroya_api.exception.InvalidValueExceptionHandler;
import com.kshrd.kroya_api.exception.NotFoundExceptionHandler;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.Category.CategoryRequest;
import com.kshrd.kroya_api.payload.Category.PaginationMeta;
import com.kshrd.kroya_api.repository.Category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public BaseResponse<?> postCategory(CategoryRequest categoryRequest) {
        String categoryName = categoryRequest.getCategoryName();
        log.info("Received request to create a category with name: {}", categoryName);

        // Validate category name to ensure it does not contain numbers, special characters, or blank spaces
        if (!categoryName.matches("^[a-zA-Z\\s]+$")) {
            log.warn("Invalid category name: '{}'. It must contain only letters and spaces.", categoryName);
            throw new InvalidValueExceptionHandler("Category name can only contain letters and spaces, without numbers or special characters.");
        }

        // Check if a category with the same name already exists
        Optional<CategoryEntity> existingCategory = categoryRepository.findByCategoryName(categoryName);
        if (existingCategory.isPresent()) {
            log.warn("Category with name '{}' already exists", categoryName);
            throw new DuplicateFieldExceptionHandler("Category name: '" + categoryName + "' already exists.");
        }

        // Save the new category to the database
        CategoryEntity categoryEntity = modelMapper.map(categoryRequest, CategoryEntity.class);
        categoryRepository.save(categoryEntity);
        log.info("Category saved successfully with ID: {}", categoryEntity.getId());

        // Build and return a successful response
        return BaseResponse.builder()
                .statusCode(String.valueOf(HttpStatus.OK.value()))
                .payload(categoryEntity)
                .message("Category has been created successfully")
                .build();
    }

//get all category with page and size and total
@Override
public BaseResponse<?> getAllCategory(Integer page, Integer size) {
    log.info("Received request to fetch all categories with page: {} and size: {}", page, size);

    // Create a Pageable object for pagination
    Pageable pageable = PageRequest.of(page, size);

    // Fetch categories from the database with pagination
    Page<CategoryEntity> categoryPage = categoryRepository.findAll(pageable);

    // Check if any categories are found
    if (categoryPage.isEmpty()) {
        log.warn("No categories found in the system");
        throw new NotFoundExceptionHandler("No categories found in the system.");
    }

    // Prepare pagination details
    long totalCategories = categoryPage.getTotalElements();
    int totalPages = categoryPage.getTotalPages();
    int currentPage = categoryPage.getNumber();

    // Construct the "next" and "previous" links
    String nextLink = (currentPage + 1 < totalPages) ?
            String.format("/api/v1/category/all?page=%d&size=%d", currentPage + 1, size) : null;
    String prevLink = (currentPage > 0) ?
            String.format("/api/v1/category/all?page=%d&size=%d", currentPage - 1, size) : null;

    // Create the PaginationMeta object
    PaginationMeta paginationMeta = new PaginationMeta(totalCategories, totalPages, currentPage, size, nextLink, prevLink);

    // Build and return the response with pagination metadata
    return BaseResponse.builder()
            .statusCode(String.valueOf(HttpStatus.OK.value()))
            .payload(categoryPage.getContent())  // List of categories in the current page
            .paginationMeta(paginationMeta)  // Include pagination details
            .message("Categories retrieved successfully")
            .build();
}

}
