package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.dtos.OrderItemsDTO;
import com.onlineshop.maxipetbackend.services.OrderItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/orderItems")
@AllArgsConstructor
public class OrderItemsController {
    private final OrderItemService orderItemService;

    /**
     * Retrieves a list of order items.
     * @return ModelAndView for displaying order items
     */
    @GetMapping("/findAllOrderItems")
    public ModelAndView getOrders(){
        List<OrderItemsDTO> orderItems = orderItemService.findOrderItems();
        ModelAndView modelAndView = new ModelAndView("orderItems");
        modelAndView.addObject("orderItems", orderItems);
        return modelAndView;
    }

    /**
     * Retrieves the order item with the specified ID.
     * @param orderItemsId The ID of the order item to retrieve
     * @return ResponseEntity containing the order item DTO
     */
    @GetMapping("/findById/{id}")
    public ResponseEntity<OrderItemsDTO> findCategory(@PathVariable("id") String orderItemsId){
        OrderItemsDTO orderItemsDTO = orderItemService.findById(orderItemsId);
        return new ResponseEntity<>(orderItemsDTO, HttpStatus.OK);
    }

    /**
     * Inserts a new order item.
     * @param orderItemsDTO The DTO representing the order item to insert
     * @return ModelAndView for redirecting to the order item listing page
     */
    @PostMapping("/insertOrderItem")
    public ModelAndView insetOrderItem(@Valid OrderItemsDTO orderItemsDTO){
        String orderItemsId = orderItemService.insertOrderItems(orderItemsDTO);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/orderItems/findAllOrderItems");
        mav.addObject("orderItems", orderItemsDTO);
        return mav;
    }

    /**
     * Removes an order item.
     * @param orderItemsId The ID of the order item to remove
     * @return ModelAndView for deleting the order item and redirecting to the order item listing page
     */
    @PostMapping ("/removeOrderItem/{id}")
    public ModelAndView removeOrderItem(@PathVariable("id") String orderItemsId){
        System.err.println(orderItemsId);
        return deleteOrderItem(orderItemsId);
    }

    /**
     * Deletes an order item.
     * @param orderItemsId The ID of the order item to delete
     * @return ModelAndView for redirecting to the order item listing page
     */
    @DeleteMapping("/deleteOrderItem/{id}")
    public ModelAndView deleteOrderItem(@PathVariable("id") String orderItemsId){
        orderItemService.delete(orderItemsId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/orderItems/findAllOrderItems");
        return mav;
    }

    /**
     * Updates an order item.
     * @param orderItemsId The ID of the order item to update
     * @param orderItemsDTO The object containing the new information for the order item to update
     * @return ModelAndView for redirecting to the order item listing page
     */
    @PostMapping ("/updateOrderItem/{id}")
    public ModelAndView updateOrderItemO(@PathVariable("id") String orderItemsId, OrderItemsDTO orderItemsDTO){
        return updateOrderItem(orderItemsId, orderItemsDTO);
    }

    /**
     * Updates an order item.
     * @param orderItemsId The ID of the order item to update
     * @param orderItemsDTO The object containing the new information for the order item to update
     * @return ModelAndView for redirecting to the order item listing page
     */
    @PutMapping("/updateOrderItemO/{id}")
    public  ModelAndView updateOrderItem(@PathVariable("id") String orderItemsId, OrderItemsDTO orderItemsDTO){
        OrderItemsDTO orderItemsDTO1 = orderItemService.update(orderItemsId, orderItemsDTO);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/orderItems/findAllOrderItems");
        return mav;
    }

}
