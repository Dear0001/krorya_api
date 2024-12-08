package com.kshrd.kroya_api.payload.FoodSell;

import com.kshrd.kroya_api.dto.PhotoDTO;
import com.kshrd.kroya_api.enums.FoodCardType;
import com.kshrd.kroya_api.enums.ItemType;
import com.kshrd.kroya_api.enums.PurchaseStatusType;
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
public class BuyerOrderCardResponse {
    private Long purchaseId;
    private Long foodSellId;
    private String name;
    private List<PhotoDTO> photo;
    private Integer quantity;
    private Double totalPrice;
    private LocalDateTime dateCooking;
    private Boolean isOrderable;
    @Builder.Default
    private ItemType itemType = ItemType.FOOD_SELL;
    private FoodCardType foodCardType;
    private PurchaseStatusType purchaseStatusType;
    private LocalDateTime purchaseDate;
}
