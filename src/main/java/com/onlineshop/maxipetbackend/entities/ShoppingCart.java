package com.onlineshop.maxipetbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "shopping_cart")
@Builder
public class ShoppingCart {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount = 0.0;

    @OneToOne(mappedBy = "shoppingCart")
    @JoinColumn(name = "user_id")
    private LocalUser localUser;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShoppingCartItem> shoppingCartItems;
}
