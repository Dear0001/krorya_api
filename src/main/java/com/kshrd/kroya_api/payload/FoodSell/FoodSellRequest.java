package com.kshrd.kroya_api.payload.FoodSell;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodSellRequest {

    private LocalDateTime dateCooking;
    private Integer amount;
    private Double price;
}
