package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.OrderLogger;
import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.OrderDTO;

import com.onlineshop.maxipetbackend.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.JsonObject;



import java.util.Collections;
import java.util.UUID;

@Controller
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/customerOrder")
public class CreateOrderController {
    private final OrderService orderService;

    /**
     * Metodă pentru obținerea informațiilor legate de comanda utilizatorului.
    // * @param id ID-ul utilizatorului pentru care se caută comanda
     * @return ModelAndView pentru afișarea paginii de plasare a comenzii
     */
    @GetMapping("/findOrderItemsByUser/")
    public ModelAndView getOrder() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("placeOrder");
        return modelAndView;
    }

    /**
     * Metodă pentru plasarea unei noi comenzi de către un utilizator.
     //* @param id ID-ul utilizatorului care plasează comanda
     * @param orderDTO obiectul care conține informațiile despre comanda de plasat
     * @return ModelAndView pentru afișarea paginii de plasare a comenzii, împreună cu detaliile comenzii plasate
     */
    @PostMapping("/placeOrder/")
    public ModelAndView placeOrder(OrderDTO orderDTO, String invoiceFormat) {
        ModelAndView modelAndView = new ModelAndView();
        OrderDTO order = orderService.placeOrder(UserConstant.userDTO.getId(), orderDTO, invoiceFormat);
        if(order != null) {
            modelAndView.setViewName("redirect:/ordersPlaced/ordersPlaced/?messageOrderPlaced=Order+placed+successfully");
        } else {
            modelAndView.setViewName("redirect:/ordersPlaced/ordersPlaced/?messageNotOrderPlaced=Something+went+wrong");
        }
        String name = "email";
        UUID nameUUID = UUID.nameUUIDFromBytes(name.getBytes());
        String email = nameUUID.toString();
        String apiUrl = "http://localhost:8081/send/insertA";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(UserConstant.userDTO.getId() + "_" + email);
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

        JsonObject mailDataJson = new JsonObject();
        mailDataJson.addProperty("toEmail", UserConstant.userDTO.getEmail());
        mailDataJson.addProperty("subject", "Order confirmation");
        mailDataJson.addProperty("body", OrderLogger.orderMessage);
        mailDataJson.addProperty("invoiceType", invoiceFormat);

        String jsonString = mailDataJson.toString();

        requestBody.add("mailData", jsonString);

        System.out.println("Conținutul requestBody:");
        System.out.println(requestBody);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        return modelAndView;
    }


}
