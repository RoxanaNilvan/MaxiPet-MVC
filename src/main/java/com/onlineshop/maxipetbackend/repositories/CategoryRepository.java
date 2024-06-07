package com.onlineshop.maxipetbackend.repositories;

import com.onlineshop.maxipetbackend.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findByAnimal(String animal);
    List<Category> findByName(String name);
    Category findByAnimalAndName(String animal, String name);

    @Query("SELECT DISTINCT c.name FROM Category c")
    List<String> findAllCategoryNames();

    @Query("SELECT DISTINCT c.animal FROM Category c")
    List<String> findAllAnimalTypes();
}
