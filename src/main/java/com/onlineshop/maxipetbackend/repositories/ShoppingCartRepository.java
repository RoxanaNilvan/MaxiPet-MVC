package com.onlineshop.maxipetbackend.repositories;

import com.onlineshop.maxipetbackend.entities.OrderItems;
import com.onlineshop.maxipetbackend.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {
    ShoppingCart searchShoppingCartByLocalUser_Id(String id);
}
