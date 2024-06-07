package com.onlineshop.maxipetbackend.repositories;

import com.onlineshop.maxipetbackend.entities.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews, String> {
    List<Reviews> findByLocalUser_Id(String userId);
    List<Reviews> findByProduct_Id(String productId);
}
