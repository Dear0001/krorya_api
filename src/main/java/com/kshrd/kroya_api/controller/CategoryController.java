package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.Category.CategoryRequest;
import com.kshrd.kroya_api.service.Category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "https://krorya-eosin.vercel.app"})
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "📂 Post a New Category",
            description = """
                    Creates a new category in the system.
                    **📩 Request Body**:
                    - **categoryRequest**: JSON object containing the details of the category to be created.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Category created successfully.
                    - **400**: 🚫 Invalid data provided.
                    """
    )
    @PostMapping("/post-category")
    public BaseResponse<?> postCategory(@RequestBody CategoryRequest categoryRequest) {
        return categoryService.postCategory(categoryRequest);
    }
    @Operation(
            summary = "📂 Get All Categories with Pagination",
            description = """
                Fetches a list of all available categories in the system with pagination.
                
                **📩 Response Summary**:
                - **200**: ✅ List of categories retrieved successfully with pagination.
                - **404**: 🚫 No categories found in the system.
                """
        )
    @GetMapping("/all")
    public BaseResponse<?> getAllCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return categoryService.getAllCategory(page, size);
    }
}

