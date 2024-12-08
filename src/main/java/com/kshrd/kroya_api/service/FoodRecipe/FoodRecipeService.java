package com.kshrd.kroya_api.service.FoodRecipe;

import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.FoodRecipe.FoodRecipeRequest;

public interface FoodRecipeService {
    BaseResponse<?> createRecipe(FoodRecipeRequest foodRecipeRequest);

    BaseResponse<?> getAllFoodRecipes();

    BaseResponse<?> editRecipe(Long recipeId, FoodRecipeRequest foodRecipeRequest);

    BaseResponse<?> getFoodRecipeByCuisineID(Long cuisineId);

    BaseResponse<?> searchFoodsByName(String name);
}
