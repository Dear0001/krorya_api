package com.kshrd.kroya_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "favorite_tb")
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "food_recipe_id", nullable = true)
    private FoodRecipeEntity foodRecipe;

    @ManyToOne
    @JoinColumn(name = "food_sell_id", nullable = true)
    private FoodSellEntity foodSell;

    @Column(name = "favorite_date", nullable = false)
    private LocalDateTime favoriteDate;
}
