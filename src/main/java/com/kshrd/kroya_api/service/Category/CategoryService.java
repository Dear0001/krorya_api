package com.kshrd.kroya_api.service.Category;

import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.Category.CategoryRequest;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    BaseResponse<?> postCategory(CategoryRequest categoryRequest);
    BaseResponse<?> getAllCategory();
}
