package com.onlineshop.maxipetbackend.entities;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartItem {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "quantity", nullable = false)
    @PositiveOrZero
    private int quantity;

    @Column(name = "price", nullable = false)
    @PositiveOrZero
    private Double price;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
