package com.onlineshop.maxipetbackend.repositories;

import com.onlineshop.maxipetbackend.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    List<Person> findByName(String name);
}
