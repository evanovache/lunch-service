package com.etz.foodapp.order.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.etz.foodapp.attendance.ClockInRecordRepository;
import com.etz.foodapp.auth.User;
import com.etz.foodapp.common.jpa.TransactionExecutor;
import com.etz.foodapp.common.time.TimeProvider;
import com.etz.foodapp.menu.MenuItem;
import com.etz.foodapp.order.Order;
import com.etz.foodapp.order.OrderItem;
import com.etz.foodapp.order.OrderSource;
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

    public void placeOrder(
        User user, 
        Long vendorId, 
        List<MenuItem> menuItems, 
        String customNote
    ) {

        TransactionExecutor txExecutor = new TransactionExecutor(em);

        txExecutor.executeVoid(entityManager -> {

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
                        vendorId, 
                        LocalDate.now()
                    )
                    .isPresent();

            if (exists) {
                throw new IllegalStateException("Order already placed.");
            }

            Order order = new Order(
                    user, 
                    em.getReference(
                        Vendor.class, 
                        vendorId
                    ),
                    customNote, 
                    OrderSource.EMPLOYEE,
                    timeProvider.currentDate(),
                    timeProvider.currentDateTime()
            );

            orderRepo.save(order);

            for (MenuItem menuItem: menuItems) {
                OrderItem oi = new OrderItem(order, menuItem, 1);
                itemRepo.save(oi);
            }
        });
            
    }
}
