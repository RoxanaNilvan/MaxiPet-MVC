package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.services.ShoppingCartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/cart")
public class AddToCartController {
    private final ShoppingCartService shoppingCartService;

    /**
     * Method for adding a product to a user's shopping cart.
     * @param productId The ID of the product to be added to the cart
     * @param quantity The quantity of products to be added to the cart
     * @return ModelAndView redirecting to the user's main page and may contain confirmation messages
     */
    @PostMapping("/addToCart")
    public ModelAndView addToCart(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity) {
        String userId = UserConstant.userDTO.getId();
        System.out.println(userId);
        String message = shoppingCartService.addToCart(userId, productId, quantity);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/customer");
        if(message.equals("Quantity is not enough")){
            modelAndView.addObject("messageNotEnough", "Quantity is not enough");
        }
        if(message.equals("Product added to the cart")){
            modelAndView.addObject("messageAddedToCart", "Product added to the cart");
        }
        return modelAndView;
    }
}
