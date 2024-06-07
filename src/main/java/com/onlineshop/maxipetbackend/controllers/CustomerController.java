package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.ProductDTO;
import com.onlineshop.maxipetbackend.dtos.ReviewsDTO;
import com.onlineshop.maxipetbackend.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Controller
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/")
public class CustomerController {
    private final ProductService productService;

    /**
     * Method to obtain the customer actions page and display available products.
     * @return ModelAndView for displaying the customer actions page and available products
     */
    @GetMapping("/customer")
    public ModelAndView getProductsPage(){
        List<String> categoryNames = productService.getAllCategoryNames();
        List<String> animalTypes = productService.getAllAnimalTypes();
        List<ProductDTO> productsC = productService.findProducts();
        ModelAndView modelAndView = new ModelAndView("customerActions");
        modelAndView.addObject("customerActions", productsC);
        modelAndView.addObject("categoryNames", categoryNames);
        modelAndView.addObject("animalTypes", animalTypes);
        if (!Objects.equals(UserConstant.userDTO.getRole(), "client")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return modelAndView;
    }

    /**
     * Filters products based on the specified category and animal type.
     * @param categoryName The category name to filter by
     * @param categoryAnimal The animal type to filter by
     * @return ModelAndView for displaying filtered products
     */
    @GetMapping("/products/filter")
    public ModelAndView filterProducts(@RequestParam(required = false) String categoryName, @RequestParam(required = false) String categoryAnimal) {
        List<String> categoryNames = productService.getAllCategoryNames();
        List<String> animalTypes = productService.getAllAnimalTypes();
        ModelAndView modelAndView = new ModelAndView("customerActions");
        modelAndView.addObject("categoryNames", categoryNames);
        modelAndView.addObject("animalTypes", animalTypes);

        if (categoryName != null && categoryAnimal != null) {
            List<ProductDTO> productsC = productService.findProductsByCategory(categoryName, categoryAnimal);
            modelAndView.addObject("customerActions", productsC);
        }

        if (!Objects.equals(UserConstant.userDTO.getRole(), "client")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return modelAndView;
    }

    /**
     * Sorts products by price.
     * @param sortOrder The sort order (ASC or DESC)
     * @return ModelAndView for displaying sorted products
     */
    @GetMapping("/products/sort")
    public ModelAndView sortProducts(@RequestParam(required = false) String sortOrder) {
        ModelAndView modelAndView = new ModelAndView("customerActions");

        if (!Objects.equals(UserConstant.userDTO.getRole(), "client")) {
            return new ModelAndView("redirect:/access-denied");
        }

        List<ProductDTO> sortedProducts = productService.sortProductsByPrice(sortOrder);
        modelAndView.addObject("customerActions", sortedProducts);
        return modelAndView;
    }

    /**
     * Displays the details of a specific product.
     * @param id The ID of the product to display
     * @param model The model to add attributes to
     * @return The view name for displaying product details
     */
    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable String id, Model model) {
        ProductDTO product = productService.findProductById(id);
        model.addAttribute("product", product);
        similarProduct(id, model);
        findReviewsByProduct(id, model);
        return "productDetails";
    }

    /**
     * Displays similar products to the specified product.
     * @param id The ID of the product to find similar products for
     * @param model The model to add attributes to
     * @return The view name for displaying similar products
     */
    @GetMapping("/similarProducts/{id}")
    public String similarProduct(@PathVariable String id, Model model){
        List<ProductDTO> productDTOList = productService.findSimilarCategory(id);
        model.addAttribute("similarProducts", productDTOList);
        return "productDetails";
    }

    /**
     * Searches for products based on the specified search query.
     * @param searchQuery The search query to use
     * @param model The model to add attributes to
     * @return ModelAndView for displaying search results
     */
    @GetMapping("/products/search")
    public ModelAndView searchProducts(@RequestParam("searchQuery") String searchQuery, Model model) {
        List<ProductDTO> products = productService.searchProducts(searchQuery);
        ModelAndView modelAndView = new ModelAndView("customerActions");
        modelAndView.addObject("customerActions", products);
        return modelAndView;
    }

    /**
     * Finds reviews for the specified product.
     * @param productId The ID of the product to find reviews for
     * @param model The model to add attributes to
     * @return The view name for displaying product reviews
     */
    @GetMapping("/findByProduct/{id}")
    public String findReviewsByProduct(@PathVariable("id") String productId, Model model) {
        List<ReviewsDTO> reviewsDTOS = productService.findAllReviewsForProduct(productId);
        model.addAttribute("reviews", reviewsDTOS);
        return "productDetails";
    }

}
