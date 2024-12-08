package com.kshrd.kroya_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cusine_tb")
@NoArgsConstructor
@Data
public class CuisineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "cuisine_name", nullable = false)
    private String cuisineName;

    @OneToMany(mappedBy = "cuisine")
    @JsonIgnore
    private List<FoodRecipeEntity> recipes;

}
