package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.ShoppingCartItemDTO;
import com.onlineshop.maxipetbackend.services.ShoppingCartItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Controller
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/items")
public class ViewCartController {
    private final ShoppingCartItemService shoppingCartItemService;

    /**
     * Method to retrieve all items from the shopping cart for a specific user.
     * @return ModelAndView for displaying the page with the shopping cart items of the given user
     */
    @GetMapping("/findAllByUser/")
    public ModelAndView getShoppingCartItemsByUserId(){
        ModelAndView modelAndView = new ModelAndView("shoppingCartItems");
        List<ShoppingCartItemDTO> items = shoppingCartItemService.findShoppingCartItemsByUserId(UserConstant.userDTO.getId());
        modelAndView.addObject("shoppingCartItems", items);
        if (!Objects.equals(UserConstant.userDTO.getRole(), "client")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return modelAndView;
    }

    /**
     * Method to remove an item from the shopping cart.
     * @param itemId the ID of the item to remove
     * @return ModelAndView for redirecting to the page with the updated shopping cart items
     */
    @PostMapping("/remove/{itemId}")
    public ModelAndView removeShoppingCartItem(@PathVariable String itemId) {
        return deleteShoppingCartItem(UserConstant.userDTO.getId(), itemId);
    }

    /**
     * Method to delete an item from the shopping cart.
     * @param userId the ID of the user whose shopping cart contains the item to delete
     * @param itemId the ID of the item to delete
     * @return ModelAndView for redirecting to the page with the updated shopping cart items
     */
    @DeleteMapping("/deleteShoppingCartIte/{itemId}")
    public ModelAndView deleteShoppingCartItem(String userId, @PathVariable String itemId) {
        shoppingCartItemService.delete(itemId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/items/findAllByUser/");
        return mav;
    }

    /**
     * Method to update the quantity of an item in the shopping cart.
     * @param id the ID of the item to update
     * @param newQuantity the new quantity of the item
     * @return ModelAndView for redirecting to the page with the updated shopping cart items
     */
    @PostMapping("/update/{id}")
    public ModelAndView updateShoppingCartItem(@PathVariable String id,@RequestParam int newQuantity) {
        return updateShoppingCartItemO(id, UserConstant.userDTO.getId(), newQuantity);
    }

    /**
     * Method to update the quantity of an item in the shopping cart.
     * @param id the ID of the item to update
     * @param userId the ID of the user whose shopping cart contains the item to update
     * @param newQuantity the new quantity of the item
     * @return ModelAndView for redirecting to the page with the updated shopping cart items
     */
    @PutMapping("/updateShoppingCartItemO/{id}")
    public ModelAndView updateShoppingCartItemO(@PathVariable String id, String userId, @RequestParam int newQuantity) {
        System.err.println(id);
        shoppingCartItemService.update(id, newQuantity);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/items/findAllByUser/");
        return mav;
    }

    /**
     * Method to remove all items from a user's shopping cart.
     * @return ModelAndView for redirecting to the page with an empty shopping cart
     */
    @PostMapping("/removeAll/")
    public ModelAndView removeAllShoppingCartItem() {
        return deleteAllShoppingCartItem(UserConstant.userDTO.getId());
    }

    /**
     * Method to remove all items from a user's shopping cart.
     * @param userId the ID of the user whose shopping cart to empty
     * @return ModelAndView for redirecting to the page with an empty shopping cart
     */
    @DeleteMapping("/deleteAllShoppingCartItem")
    public ModelAndView deleteAllShoppingCartItem(String userId) {
        shoppingCartItemService.deleteAll(userId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/items/findAllByUser/");
        return mav;
    }
}
