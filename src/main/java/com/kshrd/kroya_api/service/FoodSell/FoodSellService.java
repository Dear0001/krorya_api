package com.kshrd.kroya_api.service.FoodSell;

import com.kshrd.kroya_api.enums.CurrencyType;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.FoodSell.FoodSellRequest;

public interface FoodSellService {
    BaseResponse<?> createFoodSell(FoodSellRequest foodRecipeRequest, Long foodRecipeId, CurrencyType currencyType);

    BaseResponse<?> getAllFoodSells();

    BaseResponse<?> editFoodSell(Long foodSellId, FoodSellRequest foodSellRequest);

    BaseResponse<?> getFoodSellByCuisineID(Long cuisineId);

    BaseResponse<?> searchFoodsByName(String name);
}
