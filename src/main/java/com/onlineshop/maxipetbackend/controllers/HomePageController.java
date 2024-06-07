package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.dtos.ProductDTO;
import com.onlineshop.maxipetbackend.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@CrossOrigin
@AllArgsConstructor
public class HomePageController {
    private final ProductService productService;

    /**
     * Method to obtain the main page and display available products.
     * @return ModelAndView for displaying the main page and available products
     */
    @GetMapping("/")
    public ModelAndView getProducts(){
        List<ProductDTO> productsH = productService.findProducts();
        ModelAndView modelAndView = new ModelAndView("homePage");
        modelAndView.addObject("homePage", productsH);
        return modelAndView;
    }
}
