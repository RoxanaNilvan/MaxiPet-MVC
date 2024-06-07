package com.onlineshop.maxipetbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "local_order")
@Builder
public class LocalOrder {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "total", nullable = false)
    @PositiveOrZero
    private Double totalAmount;

    @Column(name = "addres", nullable = false)
    private String address;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "card_number")
    @NotEmpty()
    private String cardNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private LocalUser user;

    @OneToMany(mappedBy = "localOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItems> orderItems;

}
