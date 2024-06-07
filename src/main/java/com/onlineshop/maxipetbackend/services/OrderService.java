package com.onlineshop.maxipetbackend.services;


import com.onlineshop.maxipetbackend.constants.OrderLogger;
import com.onlineshop.maxipetbackend.constants.ShoppingCartLogger;
import com.onlineshop.maxipetbackend.dtos.OrderDTO;
import com.onlineshop.maxipetbackend.dtos.mappers.OrderMapper;
import com.onlineshop.maxipetbackend.entities.*;
import com.onlineshop.maxipetbackend.services.invoice.*;
import com.onlineshop.maxipetbackend.services.payment.PaymentMethod;
import com.onlineshop.maxipetbackend.services.payment.PaymentMethodFactory;
import com.onlineshop.maxipetbackend.repositories.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.onlineshop.maxipetbackend.validators.OrderValidators;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    public static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductRepository productRepository;
    private static InvoiceGenerator invoiceGenerator;

    /**
     * Retrieves a list of OrderDTO objects representing the orders.
     * @return a list of OrderDTO objects
     */
    public List<OrderDTO> findOrders(){
        List<LocalOrder> ordersList = orderRepository.findAll();
        return ordersList.stream()
                .map(OrderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the order with the specified ID.
     * @param id the ID of the order to retrieve
     * @return the OrderDTO object representing the found order
     */
    public OrderDTO findById(String id){
        Optional<LocalOrder> optionalOrder = orderRepository.findById(id);
        if(!optionalOrder.isPresent()){
            LOGGER.error(OrderLogger.ORDER_WITH_ID_NOT_FOUND, id);
        }
        return OrderMapper.toOrderDTO(optionalOrder.get());
    }

    /**
     * Inserts a new order into the database.
     * @param orderDTO the OrderDTO object containing the information about the order to insert
     * @return the ID of the inserted order
     */
    public String insertOrder(OrderDTO orderDTO){
        LocalOrder order = OrderMapper.toEntity(orderDTO);
        Optional<LocalUser> user = userRepository.findById(orderDTO.getUserId());
        order.setUser(user.get());
        order= orderRepository.save(order);
        LOGGER.debug(OrderLogger.ORDER_WITH_ID_INSERTED, order.getId());
        return order.getId();
    }

    /**
     * Deletes an order from the database.
     * @param id the ID of the order to delete
     */
    public void delete(String id){
        Optional<LocalOrder> optionalOrder = orderRepository.findById(id);
        if(!optionalOrder.isPresent()){
            LOGGER.error(OrderLogger.ORDER_WITH_ID_NOT_FOUND, id);
        }else {
            orderRepository.deleteById(id);
            LOGGER.debug(OrderLogger.ORDER_WITH_ID_DELETED, id);
        }
    }

    /**
     * Updates an order in the database.
     * @param id the ID of the order to update
     * @param orderDTO the OrderDTO object containing the new information for the order to update
     * @return the OrderDTO object representing the updated order
     */
    public OrderDTO update(String id, OrderDTO orderDTO) {
        Optional<LocalOrder> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            LocalOrder existingOrder = optionalOrder.get();
            if (orderDTO.getDate() != null) {
                existingOrder.setDate(orderDTO.getDate());
            }
            if (orderDTO.getTotalAmount() != null) {
                existingOrder.setTotalAmount(orderDTO.getTotalAmount());
            }
            existingOrder = orderRepository.save(existingOrder);
            LOGGER.debug(OrderLogger.ORDER_WITH_ID_UPDATED, existingOrder.getId());
            return OrderMapper.toOrderDTO(existingOrder);
        } else {
            LOGGER.error(OrderLogger.ORDER_WITH_ID_NOT_FOUND, id);
        }
        return OrderMapper.toOrderDTO(optionalOrder.get());
    }

    /**
     * Method to place an order.
     * @param userId the ID of the user placing the order
     * @param orderDTO the object containing the information about the order to place
     * @return the object representing the placed order
     */
    public OrderDTO placeOrder(String userId, OrderDTO orderDTO, String invoiceType) {
        ShoppingCart shoppingCart = shoppingCartRepository.searchShoppingCartByLocalUser_Id(userId);
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemRepository.findAllByShoppingCartId(shoppingCart.getId());
        LocalUser localUser = userRepository.findById(userId).get();
        double totalAmount = 0;
        LocalOrder localOrder = OrderMapper.toEntity(orderDTO);
        localOrder.setUser(localUser);
        localOrder.setDate(LocalDate.now());
        localOrder.setTotalAmount(totalAmount);
        localOrder.setStatus("placed");
        if(orderDTO.getPaymentMethod().equals("CashOnDelivery")){
            localOrder.setCardNumber("Payment on delivery");
        }

        if (!OrderValidators.validateOrder(localOrder)) {
            LOGGER.error("Validation error: Invalid order data");
            return null;
        }

        localOrder = orderRepository.save(localOrder);
        for (ShoppingCartItem shoppingCartItem : shoppingCartItems) {
            OrderItems orderItem = new OrderItems();
            orderItem.setLocalOrder(localOrder);
            orderItem.setQuantity(shoppingCartItem.getQuantity());
            orderItem.setProduct(shoppingCartItem.getProduct());
            orderItem.setPrice(shoppingCartItem.getPrice());
            Product product = shoppingCartItem.getProduct();
            product.setStock(product.getStock() - shoppingCartItem.getQuantity());
            productRepository.save(product);
            orderItemsRepository.save(orderItem);
            totalAmount += orderItem.getPrice();
        }
        localOrder.setTotalAmount(totalAmount);
        localOrder = orderRepository.save(localOrder);


        PaymentMethod paymentMethod = PaymentMethodFactory.createPaymentMethod(orderDTO.getPaymentMethod(), orderDTO.getCardNumber());
        String result = paymentMethod.pay(totalAmount);
        OrderLogger.orderMessage = result;

        emptyShoppingCart(userId);
        Invoice invoice = createInvoice(userId, localOrder);
        String generatedInvoice;
        if (invoiceType.equals("txt")) {
            invoiceGenerator = new TextInvoiceGenerator();
            generatedInvoice = invoiceGenerator.generateInvoice(invoice);
        } else {
            invoiceGenerator = new PdfInvoiceGenerator();
            generatedInvoice = invoiceGenerator.generateInvoice(invoice);
        }
        LOGGER.info(OrderLogger.ORDER_WITH_ID_INSERTED, localOrder.getId());
        return orderDTO;
    }

    /**
     * Creates an invoice for the specified user and order.
     * @param userId the ID of the user for whom the invoice is created
     * @param order the order for which the invoice is created
     * @return the Invoice object representing the created invoice
     */
    public Invoice createInvoice(String userId, LocalOrder order) {
        Invoice invoice = new Invoice();
        LocalUser localUser = userRepository.findById(userId).get();
        List<OrderItems> orderItems = orderItemsRepository.findByLocalOrderId(order.getId());
        invoice.setLastName(localUser.getLastName());
        invoice.setFirstName(localUser.getFirstName());
        invoice.setAddress(order.getAddress());
        invoice.setPhone(order.getPhone());
        invoice.setEmail(localUser.getEmail());
        invoice.setOrderItemsList(orderItems);
        invoice.setTotalPrice(order.getTotalAmount());
        return invoice;
    }


    /**
     * Method to empty the shopping cart.
     * @param userId the ID of the user whose shopping cart needs to be emptied
     */
    public void emptyShoppingCart(String userId){
        ShoppingCart shoppingCart = shoppingCartRepository.searchShoppingCartByLocalUser_Id(userId);
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemRepository.findAllByShoppingCartId(shoppingCart.getId());
        shoppingCartItemRepository.deleteAll(shoppingCartItems);
        shoppingCart.setTotalAmount(0.0);
        shoppingCartRepository.save(shoppingCart);
        LOGGER.info(ShoppingCartLogger.CART_EMPTY);
    }

    /**
     * Method to find all orders placed by a specific user.
     * @param userId the ID of the user for whom to find placed orders
     * @return the list of orders placed by the given user
     */
    public List<OrderDTO> findAllOrdersByUser(String userId){
        List<LocalOrder> localOrders = orderRepository.findAllByUserId(userId);
        if(localOrders.isEmpty()){
            LOGGER.error(OrderLogger.ORDER_WITH_USER_ID_NOT_FOUND, userId);
        }else{
            LOGGER.info(OrderLogger.ORDER_WITH_USER_ID_FOUND, userId);
        }
        return localOrders.stream()
                .map(OrderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    /**
     * Method to update the status of an order.
     * @param id the ID of the order to update
     * @param status the new status of the order
     * @return the object representing the updated order
     */
    public OrderDTO updateStatus(String id, String status) {
        Optional<LocalOrder> optionalOrder = orderRepository.findById(id);
        if(!optionalOrder.isPresent()){
            LOGGER.error(OrderLogger.ORDER_WITH_ID_NOT_FOUND, id);
            return null;
        }else{
            LocalOrder localOrder = optionalOrder.get();
            localOrder.setStatus(status);
            orderRepository.save(localOrder);
            LOGGER.info(OrderLogger.ORDER_WITH_ID_UPDATED, localOrder.getId());
            return OrderMapper.toOrderDTO(localOrder);
        }
    }

    /**
     * Method to generate a CSV report of orders.
     */
    public void getOrderCSV(){
        List<LocalOrder> localOrders = orderRepository.findAll();
        CSVGenerator.generateCSVReport(localOrders);
    }
}
