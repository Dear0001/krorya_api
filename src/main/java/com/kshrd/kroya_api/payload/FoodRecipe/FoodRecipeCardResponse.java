package com.kshrd.kroya_api.payload.FoodRecipe;

import com.kshrd.kroya_api.dto.PhotoDTO;
import com.kshrd.kroya_api.dto.UserDTO;
import com.kshrd.kroya_api.entity.PhotoEntity;
import com.kshrd.kroya_api.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRecipeCardResponse {
    private Long id;
    private List<PhotoDTO> photo; // List of PhotoEntity for each card
    private String name;
    private String description;
    private String level;
    private Double averageRating;
    private Integer totalRaters;
    private Boolean isFavorite;
    private ItemType itemType = ItemType.FOOD_RECIPE;
    private UserDTO user; // Add UserDTO field
}
