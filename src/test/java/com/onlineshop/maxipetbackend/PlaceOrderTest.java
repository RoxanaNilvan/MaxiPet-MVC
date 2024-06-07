package com.onlineshop.maxipetbackend;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.OrderDTO;
import com.onlineshop.maxipetbackend.dtos.UserDTO;
import com.onlineshop.maxipetbackend.services.OrderService;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlaceOrderTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserConstant userConstant;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("testUser");
        userDTO.setEmail("testUser@example.com");
        ReflectionTestUtils.setField(UserConstant.class, "userDTO", userDTO);
    }

    @Test
    public void givenValidOrder_whenPlaceOrder_thenRedirectToOrdersPlaced() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        String invoiceFormat = "PDF";

        // Mocking UserConstant
        UserConstant.userDTO = new UserDTO();
        UserConstant.userDTO.setId("user-id");
        UserConstant.userDTO.setEmail("testUser@example.com");

        // Mocking OrderService
        when(orderService.placeOrder(any(String.class), any(OrderDTO.class), any(String.class)))
                .thenReturn(orderDTO);

        // Mocking RestTemplate response
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity<>("Email sent", HttpStatus.OK));

        mockMvc.perform(post("/customerOrder/placeOrder/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("orderDTO", orderDTO.toString()) // Assuming orderDTO has a proper toString implementation
                        .param("invoiceFormat", invoiceFormat))
                .andExpect(redirectedUrl("/ordersPlaced/ordersPlaced/?messageOrderPlaced=Order+placed+successfully"));
    }
}
