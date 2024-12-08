package com.kshrd.kroya_api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "food_sell_tb")
public class FoodSellEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "date_cooking")
    private LocalDateTime dateCooking;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "price")
    private Double price;

    @Column(name = "currency_type")
    private String currencyType;

    @Column(name = "is_orderable")
    private Boolean isOrderable;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "food_recipe_id")
    private FoodRecipeEntity foodRecipe;

    @OneToMany(mappedBy = "foodSell", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteEntity> favorites = new ArrayList<>();
}
