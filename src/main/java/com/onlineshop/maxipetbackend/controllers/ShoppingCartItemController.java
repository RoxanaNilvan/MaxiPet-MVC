package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.ShoppingCartItemDTO;
import com.onlineshop.maxipetbackend.services.ShoppingCartItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/shoppingCartItems")
public class ShoppingCartItemController {
    private final ShoppingCartItemService shoppingCartItemService;

    /**
     * Method to find all shopping cart items.
     * @return ModelAndView for displaying all shopping cart items
     */
    @GetMapping("/findAllShoppingCartItems")
    public ModelAndView findAllShoppingCartItems() {
        ModelAndView modelAndView = new ModelAndView("shoppingCartItems");
        List<ShoppingCartItemDTO> shoppingCartItem = shoppingCartItemService.getShoppingCartItems();
        modelAndView.addObject("shoppingCartItem", shoppingCartItem);
        return modelAndView;
    }

    /**
     * Method to find all shopping cart items for a specific user.
     * @return ModelAndView for displaying the customer actions page and shopping cart items
     */
    @GetMapping("/findAllByUser")
    public ModelAndView getShoppingCartItemsByUserId(){
        ModelAndView modelAndView = new ModelAndView("customerActions");
        List<ShoppingCartItemDTO> shoppingCartItems = shoppingCartItemService.findShoppingCartItemsByUserId(UserConstant.userDTO.getId());
        for (ShoppingCartItemDTO item : shoppingCartItems) {
            System.out.println(item.getProductId() + " " + item.getQuantity() + " " + item.getPrice());
        }
        modelAndView.addObject("items", shoppingCartItems);
        return modelAndView;
    }

    /**
     * Method to insert a new item into the shopping cart.
     * @param shoppingCartItemDTO The object containing information about the item to be added to the shopping cart
     * @return ModelAndView for redirecting to the page displaying all shopping cart items
     */
    @PostMapping("/insertShoppingCartItem")
    public ModelAndView insertShoppingCartItem(@Valid ShoppingCartItemDTO shoppingCartItemDTO) {
        String shoppingCartItemId = shoppingCartItemService.insertShoppingCartItem(shoppingCartItemDTO);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/shoppingCartItems/findAllShoppingCartItems");
        mav.addObject("orderItems",shoppingCartItemDTO);
        return mav;
    }

    /**
     * Method to remove an item from the shopping cart.
     * @param id The ID of the item in the shopping cart to be removed
     * @return ModelAndView for redirecting to the page displaying all shopping cart items
     */
    @PostMapping("/removeShoppingCartItem/{id}")
    public ModelAndView removeShoppingCartItem(@PathVariable String id) {
        return deleteShoppingCartItem(id);
    }

    /**
     * Method to delete an item from the shopping cart.
     * @param id The ID of the item in the shopping cart to be deleted
     * @return ModelAndView for redirecting to the page displaying all shopping cart items
     */
    @DeleteMapping("/deleteShoppingCartItem/{id}")
    public ModelAndView deleteShoppingCartItem(@PathVariable String id) {
        shoppingCartItemService.delete(id);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/shoppingCartItems/findAllShoppingCartItems");
        return mav;
    }
}
