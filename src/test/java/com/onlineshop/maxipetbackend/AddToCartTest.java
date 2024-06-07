package com.onlineshop.maxipetbackend;
import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.UserDTO;
import com.onlineshop.maxipetbackend.services.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;



@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddToCartTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @MockBean
    private UserConstant userConstant;

    @BeforeEach
    public void setUp() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("testUser");
        ReflectionTestUtils.setField(UserConstant.class, "userDTO", userDTO);
    }

    @Test
    public void givenValidProductAndQuantity_whenAddToCart_thenProductAddedSuccessfully() throws Exception {

        String productId = "testProduct";
        int quantity = 2;

        given(shoppingCartService.addToCart(anyString(), anyString(), anyInt())).willReturn("Product added to the cart");

        ResultActions response = mockMvc.perform(post("/cart/addToCart")
                .param("productId", productId)
                .param("quantity", String.valueOf(quantity))
                .contentType(MediaType.APPLICATION_JSON));


        response.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/customer?messageAddedToCart=Product+added+to+the+cart"));
    }

    @Test
    public void givenInvalidQuantity_whenAddToCart_thenReturnQuantityNotEnough() throws Exception {
        String productId = "testProduct";
        int quantity = 20;

        given(shoppingCartService.addToCart(anyString(), anyString(), anyInt())).willReturn("Quantity is not enough");

        ResultActions response = mockMvc.perform(post("/cart/addToCart")
                .param("productId", productId)
                .param("quantity", String.valueOf(quantity))
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/customer?messageNotEnough=Quantity+is+not+enough"));
    }
}
