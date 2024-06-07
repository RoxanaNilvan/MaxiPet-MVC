package com.onlineshop.maxipetbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.catalina.User;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "local_user")
@Builder
public class LocalUser {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "First name cannot be empty")
    @Pattern(regexp = "[A-Za-z]+", message = "First name must contain only letters")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotEmpty(message = "Last name cannot be empty")
    @Pattern(regexp = "[A-Za-z]+", message = "Last name must contain only letters")
    private String lastName;

    @Column(name = "user_name", nullable = false)
    @Size(min = 5, max = 50, message = "User name must be between 5 and 50 characters")
    private String userName;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "password", nullable = false)
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Column(name = "role", nullable = false)
    @NotEmpty(message = "Role cannot be empty")
    private String role;

    @OneToMany(mappedBy = "localUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reviews> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LocalOrder> localOrders;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;
}
