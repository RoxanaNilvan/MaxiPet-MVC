package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
@CrossOrigin
@AllArgsConstructor
public class AdminController {

    /**
     * Method for displaying the administration page.
     * @return ModelAndView containing the view for administrative actions
     */
    @GetMapping("/admin")
    public ModelAndView admin(){
        ModelAndView modelAndView = new ModelAndView("adminActions");
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return modelAndView;
    }

    /**
     * Method for redirecting to the product management page.
     * @return ModelAndView redirecting to the page listing all products
     */
    @GetMapping("/produce")
    public ModelAndView produce(){
        ModelAndView mav = new ModelAndView();
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        mav.setViewName("redirect:/product/findAllProducts");
        return mav;
    }

    /**
     * Method for redirecting to the category management page.
     * @return ModelAndView redirecting to the page listing all categories
     */
    @GetMapping("/category")
    public ModelAndView category(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/category/findAllCategories");
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return mav;
    }

    /**
     * Method for redirecting to the user management page.
     * @return ModelAndView redirecting to the page listing all users
     */
    @GetMapping("/users")
    public ModelAndView users(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/user/findAllUsers");
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return mav;
    }

    /**
     * Method for redirecting to the promotions management page.
     * @return ModelAndView redirecting to the page listing all promotions
     */
    @GetMapping("/promotions")
    public ModelAndView promotions(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/promotions/findAllPromotions");
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return mav;
    }

    /**
     * Method for redirecting to the orders management page.
     * @return ModelAndView redirecting to the page listing all orders
     */
    @GetMapping("/orders")
    public ModelAndView orders(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/order/findAllOrders");
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return mav;
    }

    /**
     * Method for redirecting to the reviews management page.
     * @return ModelAndView redirecting to the page listing all reviews
     */
    @GetMapping("/review")
    public ModelAndView reviews(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/reviews/findAllReviews");
        if (!Objects.equals(UserConstant.userDTO.getRole(), "admin")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return mav;
    }
}
