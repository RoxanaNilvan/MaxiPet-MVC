package com.onlineshop.maxipetbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
@Builder
public class OrderItems {
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
    @JoinColumn(name = "order_id")
    private LocalOrder localOrder;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
