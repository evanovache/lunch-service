package com.etz.foodapp.order.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.etz.foodapp.attendance.ClockInRecordRepository;
import com.etz.foodapp.auth.User;
import com.etz.foodapp.common.jpa.TransactionExecutor;
import com.etz.foodapp.common.time.TimeProvider;
import com.etz.foodapp.menu.MenuItem;
import com.etz.foodapp.order.Order;
import com.etz.foodapp.order.OrderItem;
import com.etz.foodapp.order.OrderSource;
import com.etz.foodapp.order.dto.OrderItemRequest;
import com.etz.foodapp.order.dto.OrderItemResponse;
import com.etz.foodapp.order.dto.OrderRequest;
import com.etz.foodapp.order.dto.OrderResponse;
import com.etz.foodapp.order.repository.OrderItemRepository;
import com.etz.foodapp.order.repository.OrderRepository;
import com.etz.foodapp.vendor.Vendor;

import jakarta.persistence.EntityManager;

public class OrderService {
    
    private final EntityManager em;
    private final TimeProvider timeProvider;

    public OrderService(EntityManager em, TimeProvider timeProvider) {
        this.em = em;
        this.timeProvider = timeProvider;
    }

    public OrderResponse placeOrder(User user, OrderRequest request) {

        TransactionExecutor txExecutor = new TransactionExecutor(em);

        return txExecutor.execute(entityManager -> {

            ClockInRecordRepository clockRepo = new ClockInRecordRepository(entityManager);
            OrderRepository orderRepo = new OrderRepository(entityManager);
            OrderItemRepository itemRepo = new OrderItemRepository(entityManager);

            boolean clockedIn = clockRepo 
                        .findByUserAndDate(user.getId(), timeProvider.currentDate())
                        .isPresent();

            if (!clockedIn) {
                throw new IllegalStateException("User must clock in before ordering");
            }

            if (timeProvider.currentTime().isAfter(LocalTime.of(9, 0))) {
                throw new IllegalStateException("Orders are closed for today.");
            }

            boolean exists = orderRepo 
                    .findByUserAndVendorAndDate(
                        user.getId(), 
                        request.getVendorId(), 
                        timeProvider.currentDate()
                    )
                    .isPresent();

            if (exists) {
                throw new IllegalStateException("Order already placed.");
            }

            var vendorRef = entityManager.getReference(
                                    Vendor.class,
                                    request.getVendorId());

            Order order = new Order(
                    user, 
                    vendorRef,
                    request.getCustomNote(),
                    OrderSource.EMPLOYEE,
                    timeProvider.currentDate(),
                    timeProvider.currentDateTime()
            );

            orderRepo.save(order);

            for (OrderItemRequest itemRequest : request.getItems()) {

                MenuItem menuItem = entityManager.find(
                    MenuItem.class, itemRequest.getMenuItemId()
                );

                OrderItem orderItem = new OrderItem(
                        order, 
                        menuItem, 
                        itemRequest.getQuantity()
                );

                itemRepo.save(orderItem);
            }

            entityManager.flush();

            return mapToResponse(order, entityManager);
        });           
    }


    private OrderResponse mapToResponse(Order order, EntityManager em) {

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        List<OrderItem> orderItems = em.createQuery(
            "SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId", OrderItem.class) 
            .setParameter("orderId", order.getId()) 
            .getResultList();

        for (OrderItem oi : orderItems) {
            itemResponses.add(
                new OrderItemResponse(
                    oi.getItemName(), 
                    oi.getQuantity(), 
                    oi.getPriceAtOrderTime() 
                )
            );
        }

        return new OrderResponse(
                order.getId(), 
                order.getVendor().getName(),  
                order.getOrderTime(), 
                itemResponses, 
                order.getCustomNote()
        );
    }
}
