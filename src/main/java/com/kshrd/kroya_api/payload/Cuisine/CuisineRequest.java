package com.kshrd.kroya_api.payload.Cuisine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuisineRequest {
    private String cuisineName;
}
