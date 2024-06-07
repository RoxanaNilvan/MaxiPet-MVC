package com.onlineshop.maxipetbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promotions")
@Builder
public class Promotions {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "discount", nullable = false)
    private int discount;

    @Column(name = "discount_date", nullable = false)
    private LocalDate discountDate;

    @Column(name = "initila_price", nullable = false)
    private double initialPrice;

    @Column(name = "discounted_price", nullable = false)
    private double discountedPrice;

    @OneToOne(mappedBy = "promotions")
    private Product product;
}
