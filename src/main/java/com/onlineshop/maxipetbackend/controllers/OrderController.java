package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.dtos.OrderDTO;
import com.onlineshop.maxipetbackend.services.OrderService;
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
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * Retrieves a list of orders.
     * @return ModelAndView for displaying orders
     */
    @GetMapping("/findAllOrders")
    public ModelAndView getOrders(){
        List<OrderDTO> order = orderService.findOrders();
        ModelAndView modelAndView = new ModelAndView("order");
        modelAndView.addObject("order", order);
        return modelAndView;
    }

    /**
     * Retrieves the order with the specified ID.
     * @param orderId The ID of the order to retrieve
     * @return ResponseEntity containing the order DTO
     */
    @GetMapping("findById/{id}")
    public ResponseEntity<OrderDTO> findOrder(@PathVariable("id") String orderId){
        OrderDTO orderDTO = orderService.findById(orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    /**
     * Inserts a new order.
     * @param orderDTO The DTO representing the order to insert
     * @return ModelAndView for redirecting to the order listing page
     */
    @PostMapping("/insert")
    public ModelAndView insetOrder(@Valid @RequestBody OrderDTO orderDTO){
        String orderId = orderService.insertOrder(orderDTO);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/order/findAllOrders");
        mav.addObject("order", orderDTO);
        return mav;
    }

    /**
     * Removes an order.
     * @param orderId The ID of the order to remove
     * @return ModelAndView for deleting the order and redirecting to the order listing page
     */
    @PostMapping ("/removeOrder/{id}")
    public ModelAndView removeOrder(@PathVariable("id") String orderId){
        System.err.println(orderId);
        return deleteOrder(orderId);
    }

    /**
     * Deletes an order.
     * @param orderId The ID of the order to delete
     * @return ModelAndView for redirecting to the order listing page
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteOrder(@PathVariable("id") String orderId){
        orderService.delete(orderId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/order/findAllOrders");
        return mav;
    }

    /**
     * Updates an order's status.
     * @param orderId The ID of the order to update
     * @param status The new status of the order
     * @return ModelAndView for redirecting to the order listing page
     */
    @PostMapping ("/updateOrder/{id}")
    public ModelAndView updateOrderItemO(@PathVariable("id") String orderId, String status){
        return updateOrder(orderId, status);
    }

    /**
     * Updates an order's status.
     * @param orderId The ID of the order to update
     * @param status The new status of the order
     * @return ModelAndView for redirecting to the order listing page
     */
    @PutMapping("/updateOrderO/{id}")
    public ModelAndView updateOrder(@PathVariable("id") String orderId, String status){
        OrderDTO orderDTO1 = orderService.updateStatus(orderId, status);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/order/findAllOrders");
        return mav;
    }

    /**
     * Retrieves the CSV file for orders.
     * @return ModelAndView for redirecting to the order listing page
     */
    @GetMapping("/getCSV")
    public ModelAndView getOrderCSV(){
        orderService.getOrderCSV();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/order/findAllOrders");
        return mav;
    }
}
