package com.kshrd.kroya_api.service.FoodRecipe;

import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.FoodRecipe.FoodRecipeRequest;

public interface FoodRecipeService {
    BaseResponse<?> createRecipe(FoodRecipeRequest foodRecipeRequest);

    BaseResponse<?> getAllFoodRecipes(Integer page, Integer size);

    BaseResponse<?> editRecipe(Long recipeId, FoodRecipeRequest foodRecipeRequest);

    BaseResponse<?> getFoodRecipeByCuisineID(Long cuisineId);

    BaseResponse<?> searchFoodsByName(String name);

    BaseResponse<?> deleteFoodRecipe(Long recipeId);
}
