package com.kshrd.kroya_api.repository.Receipt;

import com.kshrd.kroya_api.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
    Optional<RecipeEntity> findByPurchase_Id(Long purchaseId);
}
