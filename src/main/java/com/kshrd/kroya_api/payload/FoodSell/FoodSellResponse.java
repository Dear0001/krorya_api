package com.kshrd.kroya_api.payload.FoodSell;

import com.kshrd.kroya_api.dto.FoodRecipeDTO;
import com.kshrd.kroya_api.dto.UserDTO;
import com.kshrd.kroya_api.entity.FoodRecipeEntity;
import com.kshrd.kroya_api.enums.CurrencyType;
import com.kshrd.kroya_api.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodSellResponse {
    private Long id;
    private FoodRecipeDTO foodRecipeDTO;
    private LocalDateTime dateCooking;
    private Integer amount;
    private Double price;
    private CurrencyType currencyType;
    private Boolean status;
    private ItemType itemType = ItemType.FOOD_SELL;
    private Boolean isFavorite;
    private Boolean isOrderable;
    private LinkedHashMap<Integer, Double> ratingPercentages;
}