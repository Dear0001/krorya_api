package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.FoodRecipe.FoodRecipeRequest;
import com.kshrd.kroya_api.service.FoodRecipe.FoodRecipeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/food-recipe")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "https://krorya-eosin.vercel.app"})
public class FoodRecipeController {

    private final FoodRecipeService foodRecipeService;

    @Operation(
            summary = "🍲 Create a New Food Recipe",
            description = """
                    Creates a new food recipe in the system.
                    **📩 Request Body**:
                    - **foodRecipeRequest**: JSON object with the details of the food recipe to be created.
                    
                    **📩 Response Summary**:
                    - **201**: ✅ Food recipe created successfully.
                    - **400**: 🚫 Invalid input data provided.
                    """
    )
    @PostMapping("/post-food-recipe")
    public BaseResponse<?> createRecipe(@RequestBody FoodRecipeRequest foodRecipeRequest) {
        return foodRecipeService.createRecipe(foodRecipeRequest);
    }

    @Operation(
            summary = "📃 Get All Food Recipes",
            description = """
                    Fetches a list of all available food recipes in the system.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ List of food recipes retrieved successfully.
                    - **404**: 🚫 No food recipes found.
                    """
    )
    @GetMapping("/list")
    public BaseResponse<?> getAllFoodRecipe(@RequestParam (defaultValue = "0") int page,
                                            @RequestParam (defaultValue = "10") int size) {
        return foodRecipeService.getAllFoodRecipes( page,  size);
    }

    @Operation(
            summary = "✏️ Edit an Existing Food Recipe",
            description = """
                    Updates an existing food recipe in the system.
                    - **Path Variable**: **recipeId**: ID of the food recipe to edit.
                    - **Request Body**: **foodRecipeRequest**: JSON object with the updated details of the recipe.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Food recipe updated successfully.
                    - **404**: 🚫 Food recipe not found.
                    """
    )
    @PutMapping("/edit-food-recipe/{recipeId}")
    public BaseResponse<?> editRecipe(@PathVariable Long recipeId, @RequestBody FoodRecipeRequest foodRecipeRequest) {
        return foodRecipeService.editRecipe(recipeId, foodRecipeRequest);
    }

    @Operation(
            summary = "🍜 Get Food Recipes by Cuisine ID",
            description = """
                    Retrieves food recipes based on the specified cuisine ID.
                    - **Path Variable**: **cuisineId**: ID of the cuisine.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ List of food recipes by cuisine ID retrieved successfully.
                    - **404**: 🚫 No food recipes found for the specified cuisine ID.
                    """
    )
    @GetMapping("/cuisine/{cuisineId}")
    public BaseResponse<?> getFoodRecipeByCuisineID(@PathVariable Long cuisineId) {
        return foodRecipeService.getFoodRecipeByCuisineID(cuisineId);
    }


    @Operation(
            summary = "🔍 Search Food Recipes by Name",
            description = """
                    Searches for food recipes that match the specified name.
                    
                    **📩 Request Parameter**:
                    - **name**: Part or full name of the food recipe to search for.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Matching food recipes retrieved successfully.
                    - **404**: 🚫 No matching food recipes found for the provided name.
                    """
    )
    @GetMapping("/search")
    public BaseResponse<?> searchFoodsByName(@RequestParam String name) {
        return foodRecipeService.searchFoodsByName(name);
    }

//    Delete Food Recipe
    @Operation(
            summary = "🗑️ Delete Food Recipe",
            description = """
                    Deletes a food recipe from the system.
                    - **Path Variable**: **recipeId**: ID of the food recipe to delete.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Food recipe deleted successfully.
                    - **404**: 🚫 Food recipe not found.
                    """
    )
    @DeleteMapping("/delete/{recipeId}")
    public BaseResponse<?> deleteFoodRecipe(@PathVariable Long recipeId) {
        return foodRecipeService.deleteFoodRecipe(recipeId);
    }

}

