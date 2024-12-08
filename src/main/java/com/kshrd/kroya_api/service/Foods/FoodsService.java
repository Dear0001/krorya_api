package com.kshrd.kroya_api.service.Foods;

import com.kshrd.kroya_api.enums.ItemType;
import com.kshrd.kroya_api.payload.BaseResponse;

public interface FoodsService {
    BaseResponse<?> getAllFoodsByCategory(Long categoryId);

    BaseResponse<?> getPopularFoods();

    BaseResponse<?> getFoodDetail(Long id, ItemType itemType);

    BaseResponse<?> deleteFood(Long id, ItemType itemType);

    BaseResponse<?> searchFoodsByName(String name);

    BaseResponse<?> getAllFoods();
}

