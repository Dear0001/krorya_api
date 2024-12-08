package com.kshrd.kroya_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kshrd.kroya_api.enums.ItemType;
import com.kshrd.kroya_api.payload.FoodRecipe.CookingStep;
import com.kshrd.kroya_api.payload.FoodRecipe.Ingredient;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "food_recipe_tb")
public class FoodRecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_in_minutes")
    private Integer durationInMinutes;

    @Column(name = "level", length = 50)
    private String level;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ingredients", columnDefinition = "jsonb")
    private List<Ingredient> ingredients;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cooking_steps", columnDefinition = "jsonb")
    private List<CookingStep> cookingSteps;

    @Column(name = "total_raters")
    private Integer totalRaters;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // Add this field

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "cuisine_id")
    @JsonIgnore
    private CuisineEntity cuisine;

    @OneToMany(mappedBy = "foodRecipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PhotoEntity> photos = new ArrayList<>();

    @OneToMany(mappedBy = "foodRecipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteEntity> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "foodRecipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedbackEntity> feedbacks = new ArrayList<>();

}
