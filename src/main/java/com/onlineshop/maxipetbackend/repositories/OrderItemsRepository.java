package com.onlineshop.maxipetbackend.repositories;
import com.onlineshop.maxipetbackend.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems, String>{
    List<OrderItems> findByLocalOrderId(String orderId);
}
