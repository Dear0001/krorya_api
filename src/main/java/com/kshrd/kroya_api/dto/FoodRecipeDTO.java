package com.kshrd.kroya_api.dto;

import com.kshrd.kroya_api.enums.ItemType;
import com.kshrd.kroya_api.payload.FoodRecipe.CookingStep;
import com.kshrd.kroya_api.payload.FoodRecipe.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRecipeDTO {
    private Long id;
    private List<PhotoDTO> photo;
    private String name;
    private String description;
    private Integer durationInMinutes;
    private String level;
    private String cuisineName;
    private String categoryName;
    private List<Ingredient> ingredients;
    private List<CookingStep> cookingSteps;
    private Integer totalRaters;
    private Double averageRating;
    private LocalDateTime createdAt;
    private UserDTO user;
}