package com.kshrd.kroya_api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kshrd.kroya_api.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reciept_tb")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "paid_by")
    private PaymentType paidBy;

    @Column(name = "paid_date")
    private LocalDateTime paidDate;

    @Column(name = "reference")
    private String reference;

    @Column(name = "status")
    private String status;

    @OneToOne
    @JoinColumn(name = "purchase_id")
    private PurchaseEntity purchase;

}
