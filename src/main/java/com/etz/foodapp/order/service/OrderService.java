package com.etz.foodapp.order.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.etz.foodapp.attendance.ClockInRecordRepository;
import com.etz.foodapp.auth.User;
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

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@RequestScoped
public class OrderService {
    
    @PersistenceContext
    private EntityManager em;

    @Inject
    private TimeProvider timeProvider;

    @Transactional
    public OrderResponse placeOrder(User user, OrderRequest request) {

        ClockInRecordRepository clockRepo = new ClockInRecordRepository(em);
        OrderRepository orderRepo = new OrderRepository(em);
        OrderItemRepository itemRepo = new OrderItemRepository(em);

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

        var vendorRef = em.getReference(
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

            MenuItem menuItem = em.find(
                MenuItem.class, itemRequest.getMenuItemId()
            );

            OrderItem orderItem = new OrderItem(
                    order, 
                    menuItem, 
                    itemRequest.getQuantity()
            );

            itemRepo.save(orderItem);
        }

        em.flush();

        return mapToResponse(order, em);          
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
