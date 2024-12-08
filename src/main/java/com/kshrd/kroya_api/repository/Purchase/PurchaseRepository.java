package com.kshrd.kroya_api.repository.Purchase;

import com.kshrd.kroya_api.entity.FoodSellEntity;
import com.kshrd.kroya_api.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
    List<PurchaseEntity> findByFoodSell_FoodRecipe_User_Id(Integer id);

    List<PurchaseEntity> findByBuyer_Id(Integer id);

    long countByFoodSell(FoodSellEntity foodSell);

    int countByFoodSell_Id(Long id);

    List<PurchaseEntity> findByFoodSell_IdAndFoodSell_FoodRecipe_User_Id(Long foodSellId, Integer id);

    List<PurchaseEntity> findByFoodSell_IdInAndCreatedDateBetween(List<Long> userFoodSellIds, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    @Query("SELECT p FROM PurchaseEntity p WHERE LOWER(p.foodSell.foodRecipe.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<PurchaseEntity> findByFoodRecipeNameContaining(@Param("name") String name);

    Collection<Object> findByBuyer_IdAndFoodSell_FoodRecipe_NameContainingIgnoreCase(Integer userId, String name);
}
