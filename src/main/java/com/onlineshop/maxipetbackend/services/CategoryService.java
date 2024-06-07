package com.onlineshop.maxipetbackend.services;

import com.onlineshop.maxipetbackend.constants.CategoryLogger;
import com.onlineshop.maxipetbackend.dtos.CategoryDTO;
import com.onlineshop.maxipetbackend.dtos.mappers.CategoryMapper;
import com.onlineshop.maxipetbackend.entities.Category;
import com.onlineshop.maxipetbackend.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    public static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    /**
     * Retrieves a list of CategoryDTO objects representing the categories.
     * @return a list of CategoryDTO objects
     */
    public List<CategoryDTO> findCategories(){
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream()
                .map(CategoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the category with the specified ID.
     * @param id the ID of the category to retrieve
     * @return the CategoryDTO object representing the found category
     */
    public CategoryDTO findById(String id){
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(!optionalCategory.isPresent()){
            LOGGER.error(CategoryLogger.CATEGORY_WITH_ID_NOT_FOUND, id);
        }
        return CategoryMapper.toCategoryDTO(optionalCategory.get());
    }

    /**
     * Inserts a new category into the database.
     * @param categoryDTO the CategoryDTO object containing the information about the category to insert
     * @return the ID of the inserted category
     */
    public String insertCategory(CategoryDTO categoryDTO){
        Category category = CategoryMapper.toEntity(categoryDTO);
        category = categoryRepository.save(category);
        LOGGER.debug(CategoryLogger.CATEGORY_WITH_ID_INSERTED, category.getId());
        return category.getId();
    }

    /**
     * Updates a category in the database.
     * @param id the ID of the category to update
     * @param categoryDTO the CategoryDTO object containing the new information for the category to update
     * @return the CategoryDTO object representing the updated category
     */
    public CategoryDTO update(String id, CategoryDTO categoryDTO){
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(!optionalCategory.isPresent()){
            LOGGER.error(CategoryLogger.CATEGORY_WITH_ID_NOT_FOUND, id);
        }else{
            Category category = optionalCategory.get();
            if(categoryDTO.getName() != null && !categoryDTO.getName().isEmpty()){
                category.setName(categoryDTO.getName());
            }

            if(categoryDTO.getAnimal() != null && !categoryDTO.getAnimal().isEmpty()){
                category.setAnimal(categoryDTO.getAnimal());
            }


            category = categoryRepository.save(category);
            LOGGER.info(CategoryLogger.CATEGORY_WITH_ID_UPDATED, category.getId());
            return CategoryMapper.toCategoryDTO(category);
        }
        return CategoryMapper.toCategoryDTO(optionalCategory.get());
    }

    /**
     * Deletes a category from the database.
     * @param id the ID of the category to delete
     * @return 0 if deletion is successful, -1 otherwise
     */
    public int deleteCategory(String id){
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(!optionalCategory.isPresent()){
            LOGGER.error(CategoryLogger.CATEGORY_WITH_ID_NOT_FOUND, id);
            return -1;
        } else {
            categoryRepository.deleteById(id);
            LOGGER.info(CategoryLogger.CATEGORY_WITH_ID_UPDATED, id);
            return 0;
        }
    }
}
