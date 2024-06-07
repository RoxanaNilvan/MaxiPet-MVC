package com.onlineshop.maxipetbackend.services;

import com.onlineshop.maxipetbackend.constants.OrderItemsLogger;
import com.onlineshop.maxipetbackend.dtos.OrderItemsDTO;
import com.onlineshop.maxipetbackend.dtos.mappers.OrderItemsMapper;
import com.onlineshop.maxipetbackend.entities.OrderItems;
import com.onlineshop.maxipetbackend.entities.Product;
import com.onlineshop.maxipetbackend.repositories.OrderItemsRepository;
import com.onlineshop.maxipetbackend.repositories.OrderRepository;
import com.onlineshop.maxipetbackend.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderItemService {
    public static final Logger LOGGER = LoggerFactory.getLogger(OrderItemService.class);
    private final OrderItemsRepository orderItemsRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    /**
     * Retrieves a list of OrderItemsDTO objects representing the order items.
     * @return a list of OrderItemsDTO objects
     */
    public List<OrderItemsDTO> findOrderItems(){
        List<OrderItems> orderItemsList = orderItemsRepository.findAll();
        return orderItemsList.stream()
                .map(OrderItemsMapper::toOrderItemsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the order item with the specified ID.
     * @param id the ID of the order item to retrieve
     * @return the OrderItemsDTO object representing the found order item
     */
    public OrderItemsDTO findById(String id){
        Optional<OrderItems> optionalOrderItems = orderItemsRepository.findById(id);
        if(!optionalOrderItems.isPresent()){
            LOGGER.error(OrderItemsLogger.ORDER_ITEM_WITH_ID_NOT_FOUND, id);
        }
        return OrderItemsMapper.toOrderItemsDTO(optionalOrderItems.get());
    }

    /**
     * Inserts a new order item into the database.
     * @param orderItemsDTO the OrderItemsDTO object containing the information about the order item to insert
     * @return the ID of the inserted order item
     */
    public String insertOrderItems(OrderItemsDTO orderItemsDTO){
        OrderItems  orderItems = OrderItemsMapper.toEntity(orderItemsDTO);
        Optional<Product> product = productRepository.findById(orderItemsDTO.getProductId());
        orderItems.setProduct(product.get());
        double price = orderItemsDTO.getQuantity() * product.get().getPrice();
        orderItems.setPrice(price);
        orderItems = orderItemsRepository.save(orderItems);
        return orderItems.getId();
    }

    /**
     * Deletes an order item from the database.
     * @param id the ID of the order item to delete
     */
    public void delete(String id){
        Optional<OrderItems> optionalOrderItems = orderItemsRepository.findById(id);
        if(!optionalOrderItems.isPresent()){
            LOGGER.error(OrderItemsLogger.ORDER_ITEM_WITH_ID_NOT_FOUND, id);
        }else {
            orderItemsRepository.deleteById(id);
            LOGGER.debug(OrderItemsLogger.ORDER_ITEM_WITH_ID_DELETED, id);
        }
    }

    /**
     * Updates an order item in the database.
     * @param id the ID of the order item to update
     * @param orderItemsDTO the OrderItemsDTO object containing the new information for the order item to update
     * @return the OrderItemsDTO object representing the updated order item
     */
    public OrderItemsDTO update(String id, OrderItemsDTO orderItemsDTO) {
        Optional<OrderItems> optionalOrderItems = orderItemsRepository.findById(id);
        if (optionalOrderItems.isPresent()) {
            OrderItems existingOrderItems = optionalOrderItems.get();

            if (orderItemsDTO.getQuantity() > 0) {
                existingOrderItems.setQuantity(orderItemsDTO.getQuantity());
            }
            if (orderItemsDTO.getPrice() != null) {
                existingOrderItems.setPrice(orderItemsDTO.getPrice());
            }
            existingOrderItems = orderItemsRepository.save(existingOrderItems);
            LOGGER.debug(OrderItemsLogger.ORDER_ITEM_WITH_ID_UPDATED, existingOrderItems.getId());
            return OrderItemsMapper.toOrderItemsDTO(existingOrderItems);
        } else {
            LOGGER.error(OrderItemsLogger.ORDER_ITEM_WITH_ID_NOT_FOUND, id);
            return OrderItemsMapper.toOrderItemsDTO(optionalOrderItems.get());
        }
    }
}
