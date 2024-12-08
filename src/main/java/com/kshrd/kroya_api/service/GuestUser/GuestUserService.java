package com.kshrd.kroya_api.service.GuestUser;

import com.kshrd.kroya_api.enums.ItemType;
import com.kshrd.kroya_api.payload.BaseResponse;

public interface GuestUserService {

    BaseResponse<?> getAllFoodSells();

    BaseResponse<?>  getAllFoodRecipes();

    BaseResponse<?> getAllFoodsByCategory(Long categoryId);

    BaseResponse<?> getPopularFoods();

    BaseResponse<?> getFoodDetail(Long id, ItemType itemType);

    BaseResponse<?> searchFoodsByName(String name);

    BaseResponse<?> searchFoodsSellByName(String name);

    BaseResponse<?> getFoodRecipeByCuisineID(Long cuisineId);

    BaseResponse<?> getFoodSellByCuisineID(Long cuisineId);

    BaseResponse<?> getAllFoodName();

    BaseResponse<?> getAllFoods();

    BaseResponse<?> searchFoodsRecipeByName(String name);
}