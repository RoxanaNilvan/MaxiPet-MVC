package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.OrderItemsDTO;
import com.onlineshop.maxipetbackend.dtos.ShoppingCartDTO;
import com.onlineshop.maxipetbackend.entities.OrderItems;
import com.onlineshop.maxipetbackend.services.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/shoppingCart")
@AllArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    /**
     * Method to find all shopping carts.
     * @return ModelAndView for displaying all shopping carts
     */
    @GetMapping("/findAllShoppingCarts")
    public ModelAndView findAllShoppingCarts() {
        List<ShoppingCartDTO> shoppingCarts = shoppingCartService.findAllShoppingCarts();
        ModelAndView modelAndView = new ModelAndView("shoppingCarts");
        modelAndView.addObject("shoppingCarts", shoppingCarts);
        return modelAndView;
    }

    /**
     * Method to find a shopping cart by its ID.
     * @param id The ID of the shopping cart to find
     * @return ModelAndView for displaying details of the found shopping cart
     */
    @GetMapping("/findById/{id}")
    public ModelAndView findShoppingCartById(@PathVariable("id") String id) {
        ShoppingCartDTO shoppingCartDTO = shoppingCartService.findShoppingCartById(id);
        ModelAndView modelAndView = new ModelAndView("shoppingCartDetails");
        modelAndView.addObject("shoppingCart", shoppingCartDTO);
        return modelAndView;
    }


    /**
     * Method to create a new shopping cart.
     * @param shoppingCartDTO The object containing information about the shopping cart to be created
     * @return ModelAndView for redirecting to the page displaying all shopping carts
     */
    @PostMapping("/createShoppingCart")
    public ModelAndView createShoppingCart(@Valid ShoppingCartDTO shoppingCartDTO) {
        String shoppingCartId = shoppingCartService.createShoppingCart(shoppingCartDTO);
        return new ModelAndView("redirect:/shoppingCart/findAllShoppingCarts");
    }

    /**
     * Method to update an existing shopping cart.
     * @param id The ID of the shopping cart to update
     * @param shoppingCartDTO The object containing updated information for the shopping cart
     * @return ModelAndView for redirecting to the page displaying all shopping carts
     */
    @PutMapping("/updateShoppingCart/{id}")
    public ModelAndView updateShoppingCart(@PathVariable("id") String id, @Valid ShoppingCartDTO shoppingCartDTO) {
        ShoppingCartDTO updatedShoppingCart = shoppingCartService.updateShoppingCart(id, shoppingCartDTO);
        return new ModelAndView("redirect:/shoppingCart/findAllShoppingCarts");
    }

    /**
     * Method to delete an existing shopping cart.
     * @param id The ID of the shopping cart to delete
     * @return ModelAndView for redirecting to the page displaying all shopping carts
     */
    @DeleteMapping("/deleteShoppingCart/{id}")
    public ModelAndView deleteShoppingCart(@PathVariable("id") String id) {
        shoppingCartService.deleteShoppingCart(id);
        return new ModelAndView("redirect:/shoppingCart/findAllShoppingCarts");
    }

    /**
     * Method to get the total amount for a given user.
     * @return The total price of the shopping cart for the given user
     */
    @GetMapping("/getTotalForUser")
    public Double getTotalForUser() {
        System.out.println(UserConstant.userDTO.getId());
        Double price = shoppingCartService.findTotalAmount(UserConstant.userDTO.getId());
        return price;
    }
}
