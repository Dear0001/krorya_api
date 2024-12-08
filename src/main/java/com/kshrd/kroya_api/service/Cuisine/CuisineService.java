package com.kshrd.kroya_api.service.Cuisine;

import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.Cuisine.CuisineRequest;
import org.springframework.stereotype.Service;

@Service
public interface CuisineService {
    BaseResponse<?> postCuisine(CuisineRequest cuisineRequest);

    BaseResponse<?> getAllCuisine();
}
