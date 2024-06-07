package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.OrderDTO;
import com.onlineshop.maxipetbackend.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Controller
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/ordersPlaced")
public class ViewOrdersController {
    private final OrderService orderService;

    /**
     * Method to find all orders placed by a specific user.
     * @return ModelAndView for displaying the page with the orders placed by the given user
     */
    @GetMapping("/ordersPlaced/")
    public ModelAndView findAllOrderPlacedByUser() {
        ModelAndView modelAndView = new ModelAndView("orderPlaced");
        List<OrderDTO> orders = orderService.findAllOrdersByUser(UserConstant.userDTO.getId());
        modelAndView.addObject("orders", orders);
        modelAndView.setViewName("orderPlaced");
        if (!Objects.equals(UserConstant.userDTO.getRole(), "client")) {
            return new ModelAndView("redirect:/access-denied");
        }
        return modelAndView;
    }
}
