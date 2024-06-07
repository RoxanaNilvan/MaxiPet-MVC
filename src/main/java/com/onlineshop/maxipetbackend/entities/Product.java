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
@Table(name = "product")
@Builder
public class Product {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    @PositiveOrZero
    private double price;

    @Column(name = "stock", nullable = false)
    @PositiveOrZero
    private int stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Reviews> reviewsList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "promotion_id")
    private Promotions promotions;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItems> orderItemsList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ShoppingCartItem> shoppingCartItemsList;

    @Column(name = "image_url")
    private String imageUrl;
}

