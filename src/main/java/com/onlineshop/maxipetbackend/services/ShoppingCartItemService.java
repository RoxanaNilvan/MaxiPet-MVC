package com.onlineshop.maxipetbackend.services;

import com.onlineshop.maxipetbackend.constants.ProductLogger;
import com.onlineshop.maxipetbackend.constants.ShoppingCartItemLogger;
import com.onlineshop.maxipetbackend.constants.ShoppingCartLogger;
import com.onlineshop.maxipetbackend.dtos.ShoppingCartItemDTO;
import com.onlineshop.maxipetbackend.dtos.mappers.ShoppingCartItemMapper;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ShoppingCartItemService {
    public static final Logger LOGGER = LoggerFactory.getLogger(OrderItemService.class);
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    /**
     * Method to get all items from the shopping cart.
     * @return The list of shopping cart items
     */
    public List<ShoppingCartItemDTO> getShoppingCartItems() {
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemRepository.findAll();
        return shoppingCartItems.stream()
                .map(ShoppingCartItemMapper::toShoppingCartItemDTO)
                .collect(Collectors.toList());
    }

    /**
     * Method to find a shopping cart item by ID.
     * @param id The ID of the shopping cart item to find
     * @return The object representing the found shopping cart item, or null if not found
     */
    public ShoppingCartItemDTO findById(String id) {
        Optional<ShoppingCartItem> shoppingCartItem = shoppingCartItemRepository.findById(id);
        if (!shoppingCartItem.isPresent()) {
            LOGGER.error(ShoppingCartItemLogger.ITEM_WITH_ID_NOT_FOUND, id);
        }
        return ShoppingCartItemMapper.toShoppingCartItemDTO(shoppingCartItem.get());
    }

    /**
     * Method to insert a new item into the shopping cart.
     * @param shoppingCartItemDTO The object containing information about the item to be inserted into the shopping cart
     * @return The ID of the inserted item in the shopping cart
     */
    public String insertShoppingCartItem(ShoppingCartItemDTO shoppingCartItemDTO) {
        ShoppingCartItem shoppingCartItem = ShoppingCartItemMapper.toShoppingCartItemEntity(shoppingCartItemDTO);
        Optional<Product> product = productRepository.findById(shoppingCartItemDTO.getProductId());
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(shoppingCartItemDTO.getShoppingCartId());
        if (!product.isPresent() || !shoppingCart.isPresent()) {
            LOGGER.error(ProductLogger.PRODUCT_WITH_ID_NOT_FOUND, shoppingCartItemDTO.getProductId());
        }else{
            shoppingCartItem.setProduct(product.get());
            shoppingCartItem.setShoppingCart(shoppingCart.get());
            double price = shoppingCartItem.getProduct().getPrice() * shoppingCartItemDTO.getQuantity();
            shoppingCartItem.setPrice(price);
            shoppingCartItem = shoppingCartItemRepository.save(shoppingCartItem);
            LOGGER.debug(ShoppingCartItemLogger.ITEM_WITH_ID_INSERTED, shoppingCartItem.getId());
        }
        return shoppingCartItem.getId();
    }


    /**
     * Method to delete an item from the shopping cart.
     * @param id The ID of the item from the shopping cart to be deleted
     */
    public void delete(String id) {
        Optional<ShoppingCartItem> shoppingCartItem = shoppingCartItemRepository.findById(id);
        if (!shoppingCartItem.isPresent()) {
            LOGGER.error(ShoppingCartItemLogger.ITEM_WITH_ID_NOT_FOUND, id);
        } else {
            ShoppingCart shoppingCart = shoppingCartItem.get().getShoppingCart();
            double newPrice = shoppingCart.getTotalAmount() - shoppingCartItem.get().getPrice();
            shoppingCart.setTotalAmount(newPrice);
            shoppingCartRepository.save(shoppingCart);
            shoppingCartItemRepository.deleteById(id);
            LOGGER.debug(ShoppingCartItemLogger.ITEM_WITH_ID_DELETED, id);
        }
    }

    /**
     * Method to delete all items from the shopping cart of a user.
     * @param userId The ID of the user whose shopping cart needs to be emptied
     */
    public void deleteAll(String userId){
        List<ShoppingCartItemDTO> shoppingCartItemDTOS = findShoppingCartItemsByUserId(userId);
        ShoppingCart shoppingCart = shoppingCartRepository.searchShoppingCartByLocalUser_Id(userId);
        shoppingCartRepository.save(shoppingCart);
        for(ShoppingCartItemDTO item : shoppingCartItemDTOS){
            delete(item.getId());
        }
        LOGGER.debug(ShoppingCartLogger.CART_EMPTY);
    }

    /**
     * Method to update the quantity of a shopping cart item.
     * @param id The ID of the shopping cart item to be updated
     * @param quantity The new quantity of the item
     * @return The object representing the updated shopping cart item
     */
    public ShoppingCartItemDTO update(String id, int quantity) {
        Optional<ShoppingCartItem> optionalShoppingCartItem = shoppingCartItemRepository.findById(id);
        if (optionalShoppingCartItem.isPresent()) {
            ShoppingCartItem existingShoppingCartItem = optionalShoppingCartItem.get();

            double oldPrice = existingShoppingCartItem.getPrice();
            double newPrice = existingShoppingCartItem.getProduct().getPrice() * quantity;
            double priceDifference = newPrice - oldPrice;

            existingShoppingCartItem.setQuantity(quantity);
            existingShoppingCartItem.setPrice(newPrice);
            existingShoppingCartItem = shoppingCartItemRepository.save(existingShoppingCartItem);

            ShoppingCart shoppingCart = existingShoppingCartItem.getShoppingCart();
            double totalPrice = shoppingCart.getTotalAmount() + priceDifference;
            shoppingCart.setTotalAmount(totalPrice);
            shoppingCartRepository.save(shoppingCart);

            LOGGER.debug(ShoppingCartItemLogger.ITEM_WITH_ID_UPDATED, existingShoppingCartItem.getId());
            return ShoppingCartItemMapper.toShoppingCartItemDTO(existingShoppingCartItem);
        } else {
            LOGGER.error(ShoppingCartItemLogger.ITEM_WITH_ID_NOT_FOUND, id);
            return null;
        }
    }

    /**
     * Method to find all items from the shopping cart of a user.
     * @param userId The ID of the user for whom the shopping cart items are to be found
     * @return The list of shopping cart items of the given user
     */
    public List<ShoppingCartItemDTO> findShoppingCartItemsByUserId(String userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.searchShoppingCartByLocalUser_Id(userId);
        if (shoppingCart != null) {
            List<ShoppingCartItem> shoppingCartItems = shoppingCartItemRepository.findAllByShoppingCartId(shoppingCart.getId());
            LOGGER.info("Shopping cart items found");
            return shoppingCartItems.stream()
                    .map(ShoppingCartItemMapper::toShoppingCartItemDTO)
                    .collect(Collectors.toList());
        }else{
            LOGGER.error("User with id " + userId + " has no shopping cart items");
        }
        return Collections.emptyList();
    }
}

