package com.onlineshop.maxipetbackend.services;

import com.onlineshop.maxipetbackend.constants.ShoppingCartLogger;
import com.onlineshop.maxipetbackend.dtos.ShoppingCartDTO;
import com.onlineshop.maxipetbackend.dtos.mappers.ShoppingCartMapper;
import com.onlineshop.maxipetbackend.entities.Product;
import com.onlineshop.maxipetbackend.entities.ShoppingCart;
import com.onlineshop.maxipetbackend.entities.ShoppingCartItem;
import com.onlineshop.maxipetbackend.repositories.ProductRepository;
import com.onlineshop.maxipetbackend.repositories.ShoppingCartItemRepository;
import com.onlineshop.maxipetbackend.repositories.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShoppingCartService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartService.class);
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;

    /**
     * Method to find all shopping carts.
     * @return The list of shopping carts
     */
    public List<ShoppingCartDTO> findAllShoppingCarts() {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAll();
        return shoppingCarts.stream()
                .map(ShoppingCartMapper::toShoppingCartDTO)
                .collect(Collectors.toList());
    }

    /**
     * Method to find a shopping cart by ID.
     * @param id The ID of the shopping cart to find
     * @return The object representing the found shopping cart, or null if not found
     */
    public ShoppingCartDTO findShoppingCartById(String id) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findById(id);
        if (shoppingCartOptional.isPresent()) {
            return ShoppingCartMapper.toShoppingCartDTO(shoppingCartOptional.get());
        } else {
            LOGGER.error(ShoppingCartLogger.CART_WITH_ID_NOT_FOUND, id);
            return null;
        }
    }

    /**
     * Method to create a new shopping cart.
     * @param shoppingCartDTO The object containing information about the shopping cart to be created
     * @return The ID of the created shopping cart
     */
    public String createShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = ShoppingCartMapper.toShoppingCartEntity(shoppingCartDTO);
        shoppingCart = shoppingCartRepository.save(shoppingCart);
        LOGGER.debug(ShoppingCartLogger.CART_WITH_ID_INSERTED, shoppingCart.getId());
        return shoppingCart.getId();
    }

    /**
     * Method to update an existing shopping cart.
     * @param id The ID of the shopping cart to update
     * @param shoppingCartDTO The object containing updated information about the shopping cart
     * @return The object representing the updated shopping cart, or null if the shopping cart was not found
     */
    public ShoppingCartDTO updateShoppingCart(String id, ShoppingCartDTO shoppingCartDTO) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findById(id);
        if (shoppingCartOptional.isPresent()) {
            ShoppingCart shoppingCart = shoppingCartOptional.get();
            shoppingCart.setTotalAmount(shoppingCartDTO.getTotalAmount());
            shoppingCart = shoppingCartRepository.save(shoppingCart);
            LOGGER.debug(ShoppingCartLogger.CART_WITH_ID_UPDATED, shoppingCart.getId());
            return ShoppingCartMapper.toShoppingCartDTO(shoppingCart);
        } else {
            LOGGER.error(ShoppingCartLogger.CART_WITH_ID_NOT_FOUND, id);
            return null;
        }
    }

    /**
     * Method to delete a shopping cart.
     * @param id The ID of the shopping cart to delete
     */
    public void deleteShoppingCart(String id) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findById(id);
        if (shoppingCartOptional.isPresent()) {
            shoppingCartRepository.deleteById(id);
            LOGGER.debug(ShoppingCartLogger.CART_WITH_ID_DELETED, id);
        } else {
            LOGGER.error(ShoppingCartLogger.CART_WITH_ID_NOT_FOUND, id);
        }
    }

    /**
     * Method to add an order item to a shopping cart.
     * @param cartId The ID of the shopping cart to add the order item to
     * @param shoppingCartItem The order item to be added to the shopping cart
     */
    public void addOrderItemToCart(String cartId, ShoppingCartItem shoppingCartItem){
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(cartId);
        shoppingCart.get().getShoppingCartItems().add(shoppingCartItem);
        shoppingCart.get().setTotalAmount(shoppingCart.get().getTotalAmount() + shoppingCartItem.getPrice());
        shoppingCartRepository.save(shoppingCart.get());
    }

    /**
     * Method to add a product to a shopping cart.
     * @param userId The ID of the user for whom the product is added to the shopping cart
     * @param productId The ID of the product to be added to the shopping cart
     * @param quantity The quantity of the product to be added to the shopping cart
     * @return A message indicating the result of adding the product to the shopping cart
     */

    public String addToCart(String userId, String productId, int quantity) {
        ShoppingCart shoppingCartOptional = shoppingCartRepository.searchShoppingCartByLocalUser_Id(userId);
        String message;
        if (shoppingCartOptional != null) {
            ShoppingCart shoppingCart;
            shoppingCart = shoppingCartOptional;
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                if(product.getStock() > quantity) {
                    ShoppingCartItem shoppingCartItem = createShoppingCartItem(product, quantity);
                    shoppingCartItem.setShoppingCart(shoppingCart);
                    shoppingCartItemRepository.save(shoppingCartItem);
                    shoppingCart.getShoppingCartItems().add(shoppingCartItem);
                    shoppingCart.setTotalAmount(shoppingCart.getTotalAmount() + shoppingCartItem.getPrice());
                    shoppingCartRepository.save(shoppingCart);
                    LOGGER.info("ShoppingCartItem {} added to cart {}", shoppingCartItem.getId(), productId);
                    message = "Product added to the cart";
                }else{
                    message = "Quantity is not enough";
                    LOGGER.error("Quantity is not enough");
                }
            } else {
                message = "Product does not exist";
                LOGGER.error("Product does not exist");
            }
        } else {
            message = "Shopping cart does not exist";
            LOGGER.error("Shopping cart does not exist");
        }
        return message;
    }

    /**
     * Method to create a shopping cart item for a given product and quantity.
     * @param product The product for which the shopping cart item is created
     * @param quantity The quantity of the product for which the shopping cart item is created
     * @return The created shopping cart item
     */

    private ShoppingCartItem createShoppingCartItem(Product product, int quantity) {
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setProduct(product);
        shoppingCartItem.setQuantity(quantity);
        double price = quantity * product.getPrice();
        shoppingCartItem.setPrice(price);
        return shoppingCartItem;
    }

    /**
     * Method to find the total amount of a user's shopping cart.
     * @param userId The ID of the user for which the total amount of the shopping cart is calculated
     * @return The total amount of the given user's shopping cart
     */
    public Double findTotalAmount(String userId) {
        ShoppingCart shoppingCartOptional = shoppingCartRepository.searchShoppingCartByLocalUser_Id(userId);
        Double totalAmount = shoppingCartOptional.getTotalAmount();
        return totalAmount;
    }
}
