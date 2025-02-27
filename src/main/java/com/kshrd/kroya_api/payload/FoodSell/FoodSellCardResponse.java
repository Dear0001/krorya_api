package com.kshrd.kroya_api.payload.FoodSell;

import com.kshrd.kroya_api.dto.PhotoDTO;
import com.kshrd.kroya_api.dto.UserProfileDTO;
import com.kshrd.kroya_api.enums.CurrencyType;
import com.kshrd.kroya_api.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodSellCardResponse {
    private Long foodSellId;
    private List<PhotoDTO> photo;
    private String name;
    private LocalDateTime dateCooking;
    private Double price;
    private CurrencyType currencyType;
    private Double averageRating;
    private Integer totalRaters;
    private Boolean isFavorite;
    @Builder.Default
    private ItemType itemType = ItemType.FOOD_SELL;
    private Boolean isOrderable;
    private UserProfileDTO sellerInformation;
}