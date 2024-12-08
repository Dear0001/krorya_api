package com.kshrd.kroya_api.service.Favorite;

import com.kshrd.kroya_api.enums.ItemType;
import com.kshrd.kroya_api.payload.BaseResponse;

public interface FavoriteService {
    BaseResponse<?> saveFoodToFavorite(Long foodId, ItemType itemType);

    BaseResponse<?> unsavedFoodFromFavorite(Long foodId, ItemType itemType);

    BaseResponse<?> getAllFavoriteFoodsByCurrentUser();

    BaseResponse<?> searchFoodsByName(String name);
}
