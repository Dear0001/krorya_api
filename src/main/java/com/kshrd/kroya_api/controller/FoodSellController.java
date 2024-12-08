package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.enums.CurrencyType;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.FoodSell.FoodSellRequest;
import com.kshrd.kroya_api.service.FoodSell.FoodSellService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;

@RestController
@RequestMapping("api/v1/food-sell")
@RequiredArgsConstructor
@Slf4j
public class FoodSellController {

    private final FoodSellService foodSellService;

    @Operation(
            summary = "🍲 Create a New Food Sell",
            description = """
                    Creates a new food sell entry linked to an existing food recipe.
                    **📩 Request Body**:
                    - **foodSellRequest**: JSON object with the food sell details.
                    - **foodRecipeId**: ID of the linked food recipe.
                    - **currencyType**: Type of currency (e.g., DOLLAR, RIEL).
                    
                    **📩 Response Summary**:
                    - **201**: ✅ Food sell created successfully.
                    - **400**: 🚫 Invalid input data or duplicate food sell entry.
                    """
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/post-food-sell")
    public BaseResponse<?> createFoodSell(@RequestBody FoodSellRequest foodRecipeRequest,
                                          @RequestParam Long foodRecipeId,
                                          @RequestParam CurrencyType currencyType) {
        return foodSellService.createFoodSell(foodRecipeRequest, foodRecipeId, currencyType);
    }

    @Operation(
            summary = "📃 Get All Food Sells",
            description = """
                    Fetches a list of all available food sell entries.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ List of food sells retrieved successfully.
                    - **404**: 🚫 No food sells found.
                    """
    )
    @GetMapping("/list")
    public BaseResponse<?> getAllFoodSells() {
        return foodSellService.getAllFoodSells();
    }

    @Operation(
            summary = "✏️ Edit an Existing Food Sell",
            description = """
                    Updates an existing food sell entry.
                    - **Path Variable**: **foodSellId**: ID of the food sell entry to edit.
                    - **Request Body**: **foodSellRequest**: JSON object with updated food sell details.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Food sell updated successfully.
                    - **404**: 🚫 Food sell not found.
                    """
    )
    @PutMapping("/edit-food-sell/{foodSellId}")
    public BaseResponse<?> editFoodSell(@PathVariable Long foodSellId, @RequestBody FoodSellRequest foodSellRequest) {
        return foodSellService.editFoodSell(foodSellId, foodSellRequest);
    }

    @Operation(
            summary = "🍜 Get Food Sells by Cuisine ID",
            description = """
                    Retrieves food sell entries based on the specified cuisine ID.
                    - **Path Variable**: **cuisineId**: ID of the cuisine.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ List of food sells by cuisine ID retrieved successfully.
                    - **404**: 🚫 No food sells found for the specified cuisine ID.
                    """
    )
    @GetMapping("/cuisine/{cuisineId}")
    public BaseResponse<?> getFoodSellByCuisineID(@PathVariable Long cuisineId) {
        return foodSellService.getFoodSellByCuisineID(cuisineId);
    }

    @Operation(
            summary = "🔍 Search Food Sells by Name",
            description = """
                    Searches for food sell and recipe entries containing the specified name. 
                    Returns lists of matching food recipes and food sells.
                    - **Query Parameter**: **name**: Part or full name of the food item to search.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Search results fetched successfully.
                    - **404**: 🚫 No foods found matching the specified name.
                    """
    )
    @GetMapping("/search")
    public BaseResponse<?> searchFoodsByName(@RequestParam String name) {
        return foodSellService.searchFoodsByName(name);
    }




}
