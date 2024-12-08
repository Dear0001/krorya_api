package com.kshrd.kroya_api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponse<T> {
    private Integer statusCode;
    private T payload;
    private Integer totalProduct;
    private LocalDateTime date;
    private boolean success;
}
