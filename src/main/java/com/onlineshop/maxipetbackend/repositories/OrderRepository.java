package com.onlineshop.maxipetbackend.repositories;

import com.onlineshop.maxipetbackend.entities.LocalOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<LocalOrder, String> {
    List<LocalOrder> findAllByUserId(String userId);
}
