package com.kshrd.kroya_api.repository.Feedback;

import com.kshrd.kroya_api.entity.FeedbackEntity;
import com.kshrd.kroya_api.entity.FoodRecipeEntity;
import com.kshrd.kroya_api.entity.FoodSellEntity;
import com.kshrd.kroya_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {

    boolean existsByUserAndFoodRecipe(UserEntity currentUser, FoodRecipeEntity recipe);

    boolean existsByUserAndFoodSell(UserEntity currentUser, FoodSellEntity sell);

    boolean existsByUserAndFoodRecipeAndCommentTextIsNotNull(UserEntity currentUser, FoodRecipeEntity recipe);

    boolean existsByUserAndFoodSellAndCommentTextIsNotNull(UserEntity currentUser, FoodSellEntity sell);

    Optional<FeedbackEntity> findByUserAndFoodRecipe(UserEntity currentUser, FoodRecipeEntity recipe);

    Optional<FeedbackEntity> findByUserAndFoodSell(UserEntity currentUser, FoodSellEntity sell);

    List<FeedbackEntity> findByFoodRecipeId(Long foodId);

    List<FeedbackEntity> findByFoodSellId(Long foodId);

    List<FeedbackEntity> findByFoodRecipe(FoodRecipeEntity recipe);

    List<FeedbackEntity> findByFoodSell(FoodSellEntity sell);
}
