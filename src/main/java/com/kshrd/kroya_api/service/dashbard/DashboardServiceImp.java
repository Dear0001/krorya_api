package com.kshrd.kroya_api.service.dashbard;

import com.kshrd.kroya_api.repository.Category.CategoryRepository;
import com.kshrd.kroya_api.repository.Cuisine.CuisineRepository;
import com.kshrd.kroya_api.repository.FoodRecipe.FoodRecipeRepository;
import com.kshrd.kroya_api.repository.User.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImp implements DashboardService {
    private final UserRepository userRepository;
    private final FoodRecipeRepository foodRecipeRepository;
    private final CategoryRepository categoryRepository;
    private final CuisineRepository cuisineRepository;

    public DashboardServiceImp(UserRepository userRepository, FoodRecipeRepository foodRecipeRepository, CategoryRepository categoryRepository, CuisineRepository cuisineRepository) {
        this.userRepository = userRepository;
        this.foodRecipeRepository = foodRecipeRepository;
        this.categoryRepository = categoryRepository;
        this.cuisineRepository = cuisineRepository;
    }

    @Override
    public Map<String, Long> getCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("users", userRepository.count());
        counts.put("recipes", foodRecipeRepository.count());
        counts.put("categories", categoryRepository.count());
        counts.put("cuisines", cuisineRepository.count());
        
        return counts;
    }
}