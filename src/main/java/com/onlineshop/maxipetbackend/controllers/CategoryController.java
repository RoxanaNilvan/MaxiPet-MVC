package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.CategoryLogger;
import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.CategoryDTO;

import com.onlineshop.maxipetbackend.services.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Controller
@CrossOrigin
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Returns a list of categories.
     */
    @GetMapping("/findAllCategories")
    public ModelAndView getCategories(){
        List<CategoryDTO> categories = categoryService.findCategories();
        ModelAndView modelAndView = new ModelAndView("categories");
        modelAndView.addObject("categories", categories);
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return modelAndView;
    }

    /**
     * Returns the category with the specified ID.
     * @param categoryId The ID of the category to search for
     */
    @GetMapping("/findById/{id}")
    public ResponseEntity<CategoryDTO> findCategory(@PathVariable("id") String categoryId){
        CategoryDTO categoryDTO = categoryService.findById(categoryId);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    /**
     * Inserts a new category.
     * @param categoryDTO The object representing the category to insert
     * @return The ID of the inserted category
     */
    @PostMapping("/insertCategory")
    public ModelAndView insetCategory(@Valid CategoryDTO categoryDTO){
        String categoryId = categoryService.insertCategory(categoryDTO);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/category/findAllCategories");
        if(categoryId != null){
            mav.addObject("successInsertCategory", CategoryLogger.CATEGORY_INSERTED);
        }else{
            mav.addObject("errorInsertCategory", CategoryLogger.CATEGORY_INSERT_FAILED);
        }
        return mav;
    }

    /**
     * Updates a category.
     * @param categoryId The ID of the category to update
     * @param categoryDTO The object containing the new information for the category to update
     * @return The object representing the updated category
     */
    @PostMapping ("/updateCategory/{id}")
    public ModelAndView updateCategoryC(@PathVariable("id") String categoryId, CategoryDTO categoryDTO){
        return updateCategory(categoryId, categoryDTO);
    }

    /**
     * Updates a category.
     * @param categoryId The ID of the category to update
     * @param categoryDTO The object containing the new information for the category to update
     * @return The object representing the updated category
     */
    @PutMapping("/updateC/{id}")
    public ModelAndView updateCategory(@PathVariable("id") String categoryId, CategoryDTO categoryDTO){
        CategoryDTO category = categoryService.update( categoryId, categoryDTO);
        ModelAndView mav = new ModelAndView();
        if(category != null){
            mav.addObject("successUpdateCategory", CategoryLogger.CATEGORY_UPDATED);
        }else {
            mav.addObject("errorUpdateCategory", CategoryLogger.CATEGORY_UPDATE_FAILED);
        }
        mav.setViewName("redirect:/category/findAllCategories");
        return mav;
    }

    /**
     * Deletes a category.
     * @param categoryId The ID of the category to delete
     */
    @PostMapping ("/removeCategory/{id}")
    public ModelAndView removeCategory(@PathVariable("id") String categoryId){
        System.err.println(categoryId);
        return deleteCategory(categoryId);
    }

    /**
     * Deletes a category.
     * @param categoryId The ID of the category to delete
     */
    @DeleteMapping("/deleteCategory/{id}")
    public ModelAndView deleteCategory(@PathVariable("id") String categoryId){
        int rez = categoryService.deleteCategory(categoryId);
        ModelAndView mav = new ModelAndView();
        if(rez == 0){
            mav.addObject("successDeleteCategory", CategoryLogger.CATEGORY_DELETED);
        }else{
            mav.addObject("errorDeleteCategory", CategoryLogger.CATEGORY_DELETE_FAILED);
        }
        mav.setViewName("redirect:/category/findAllCategories");
        return mav;
    }
}
