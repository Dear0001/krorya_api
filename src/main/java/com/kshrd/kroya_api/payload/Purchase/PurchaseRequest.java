package com.kshrd.kroya_api.payload.Purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kshrd.kroya_api.entity.FoodSellEntity;
import com.kshrd.kroya_api.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseRequest {
    private Long foodSellId;
    private String remark;
    private String quantity;
    private Double totalPrice;
}
