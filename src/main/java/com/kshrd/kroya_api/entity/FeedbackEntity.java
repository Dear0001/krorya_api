package com.kshrd.kroya_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "feedback_tb")
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating_value")
    private Integer ratingValue; // Optional field for rating

    @Column(name = "comment_text", columnDefinition = "TEXT")
    private String commentText; // Optional field for comment

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "food_recipe_id")
    private FoodRecipeEntity foodRecipe;

    @ManyToOne
    @JoinColumn(name = "food_sell_id")
    private FoodSellEntity foodSell;
}
