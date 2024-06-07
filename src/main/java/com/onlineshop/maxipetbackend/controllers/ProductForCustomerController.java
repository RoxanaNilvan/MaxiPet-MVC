package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.dtos.ProductDTO;
import com.onlineshop.maxipetbackend.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/c/product")
@AllArgsConstructor
public class ProductForCustomerController {
    private final ProductService productService;

    /**
     * Retrieves all products for customers.
     * @return A ModelAndView object representing the view of products for customers
     */
    @GetMapping("/findAllProducts")
    public ModelAndView getProducts(){
        List<ProductDTO> productsC = productService.findProducts();
        ModelAndView modelAndView = new ModelAndView("products");
        modelAndView.addObject("products", productsC);
        return modelAndView;
    }
}
