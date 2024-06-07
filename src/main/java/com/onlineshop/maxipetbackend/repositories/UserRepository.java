package com.onlineshop.maxipetbackend.repositories;

import com.onlineshop.maxipetbackend.entities.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<LocalUser, String> {
    LocalUser findLocalUserByEmailAndPassword(String email, String password);
}
