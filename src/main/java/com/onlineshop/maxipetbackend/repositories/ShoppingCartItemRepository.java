package com.onlineshop.maxipetbackend.repositories;

import com.onlineshop.maxipetbackend.entities.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, String> {
    List<ShoppingCartItem> findAllByShoppingCartId(String shoppingCartId);
}
