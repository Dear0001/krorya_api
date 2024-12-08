package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.enums.ItemType;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.service.Favorite.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/favorite")
@AllArgsConstructor
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(
            summary = "⭐ Add Food to Favorites",
            description = " Adds a food item to the user's list of favorites."
    )
    @PostMapping("/add-favorite")
    public BaseResponse<?> saveFoodToFavorite(
            @RequestParam Long foodId,
            @RequestParam ItemType itemType
    ) {
        return favoriteService.saveFoodToFavorite(foodId, itemType);
    }

    @Operation(
            summary = "❌ Remove Food from Favorites",
            description = "Removes a food item from the user's list of favorites."

    )
    @DeleteMapping("/remove-favorite")
    public BaseResponse<?> removeFoodFromFavorite(
            @RequestParam Long foodId,
            @RequestParam ItemType itemType
    ) {
        return favoriteService.unsavedFoodFromFavorite(foodId, itemType);
    }

    @Operation(
            summary = "📋 Get All Favorite Foods",
            description = "Retrieves the list of all favorite foods for the currently authenticated user."
    )
    @GetMapping("/all")
    public BaseResponse<?> getAllFavoriteFoodsByCurrentUser() {
        return favoriteService.getAllFavoriteFoodsByCurrentUser();
    }

    @Operation(
            summary = "🔍 Search Favorite Foods by Name",
            description = " Searches for favorite foods by name."
    )
    @GetMapping("/search")
    public BaseResponse<?> searchFoodsByName(@RequestParam String name) {
        return favoriteService.searchFoodsByName(name);
    }
}

