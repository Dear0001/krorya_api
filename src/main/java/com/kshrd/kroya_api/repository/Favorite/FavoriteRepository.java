package com.kshrd.kroya_api.repository.Favorite;

import com.kshrd.kroya_api.entity.FavoriteEntity;
import com.kshrd.kroya_api.entity.FoodRecipeEntity;
import com.kshrd.kroya_api.entity.FoodSellEntity;
import com.kshrd.kroya_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    Optional<FavoriteEntity> findByUserAndFoodRecipe(UserEntity user, FoodRecipeEntity foodRecipe);

    Optional<FavoriteEntity> findByUserAndFoodSell(UserEntity user, FoodSellEntity foodSell);

    List<FavoriteEntity> findByUserAndFoodRecipeIsNotNull(UserEntity currentUser);

    List<FavoriteEntity> findByUserAndFoodSellIsNotNull(UserEntity currentUser);

    List<FavoriteEntity> findByUser(UserEntity currentUser);

    boolean existsByUserAndFoodRecipe(UserEntity currentUser, FoodRecipeEntity savedRecipe);

    boolean existsByUserAndFoodSell(UserEntity currentUser, FoodSellEntity savedFoodSell);
}
