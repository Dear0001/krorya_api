package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.Category.CategoryRequest;
import com.kshrd.kroya_api.payload.Cuisine.CuisineRequest;
import com.kshrd.kroya_api.service.Cuisine.CuisineService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cuisine")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "https://krorya-eosin.vercel.app"})
public class CuisineController {

    private final CuisineService cuisineService;

    @Operation(
            summary = "🍲 Post a New Cuisine",
            description = """
                    Creates a new cuisine in the system.
                    **📩 Request Body**:
                    - **cuisineRequest**: JSON object containing the details of the cuisine to be created.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Cuisine created successfully.
                    - **400**: 🚫 Invalid data provided.
                    """
    )
    @PostMapping("/post-cuisine")
    public BaseResponse<?> postCuisine(@RequestBody CuisineRequest cuisineRequest) {
        return cuisineService.postCuisine(cuisineRequest);
    }

    @Operation(
            summary = "🍜 Get All Cuisines",
            description = """
                    Fetches a list of all cuisines available in the system.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Cuisines retrieved successfully.
                    - **404**: 🚫 No cuisines found in the system.
                    """
    )
    @GetMapping("/all")
    public BaseResponse<?> getAllCuisine(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return cuisineService.getAllCuisine(page, size);
    }
}

