package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.enums.ItemType;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.service.Foods.FoodsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/foods")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "https://krorya-eosin.vercel.app"})
public class FoodsController {

    private final FoodsService foodsService;

    @Operation(
            summary = "📦 Get All Foods by Category",
            description = """
                    Fetches all foods within a specified category by `categoryId`. 
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Foods fetched successfully.
                    - **404**: ❌ No foods found for the specified category ID.
                    - **400**: 🚫 Invalid category ID format.
                    """
    )
    @GetMapping("/{categoryId}")
    public BaseResponse<?> getAllFoodsByCategory(@PathVariable Long categoryId) {
        return foodsService.getAllFoodsByCategory(categoryId);
    }

    @Operation(
            summary = "🔥 Get Popular Foods",
            description = """
                    Fetches a list of the most popular foods based on average ratings. 
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Popular foods fetched successfully.
                    """
    )
    @GetMapping("/popular")
    public BaseResponse<?> getPopularFoods() {
        return foodsService.getPopularFoods();
    }

    @Operation(
            summary = "🔍 Get Food Details by ID",
            description = """
                    Retrieves detailed information for a food item by its `id` and `itemType`.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Food details fetched successfully.
                    - **404**: ❌ Food item not found.
                    - **400**: 🚫 Invalid item type.
                    """
    )
    @GetMapping("/detail/{id}")
    public BaseResponse<?> getFoodDetail(
            @PathVariable Long id,
            @RequestParam ItemType itemType) {
        return foodsService.getFoodDetail(id, itemType);
    }

    @Operation(
            summary = "🗑️ Delete Food by ID",
            description = """
                    Deletes a food item by its `id` and `itemType`.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Food item deleted successfully.
                    - **404**: ❌ Food item not found.
                    - **400**: 🚫 Invalid item type.
                    """
    )
    @DeleteMapping("/delete/{id}")
    public BaseResponse<?> deleteFood(
            @PathVariable Long id,
            @RequestParam ItemType itemType) {
        return foodsService.deleteFood(id, itemType);
    }

    @Operation(
            summary = "🔍 Search Foods by Name",
            description = """
                    Searches for foods that match the specified `name`. 
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Search results fetched successfully.
                    - **404**: ❌ No foods found matching the search term.
                    """
    )
    @GetMapping("/search")
    public BaseResponse<?> searchFoodsByName(@RequestParam String foodName) {
        return foodsService.searchFoodsByName(foodName);
    }

    @Operation(
            summary = "📋 Get All Foods",
            description = """
                    Retrieves a list of all available food items.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ All foods fetched successfully.
                    """
    )
    @GetMapping("/list")
    public BaseResponse<?> getAllFoods() {
        return foodsService.getAllFoods();
    }
}

