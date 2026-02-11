package com.etz.foodapp.order.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.etz.foodapp.auth.User;
import com.etz.foodapp.menu.MenuItem;
import com.etz.foodapp.order.Order;
import com.etz.foodapp.order.OrderItem;
import com.etz.foodapp.order.OrderSource;
import com.etz.foodapp.order.repository.OrderItemRepository;
import com.etz.foodapp.order.repository.OrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class OrderService {
    
    private final EntityManager em;

    public OrderService(EntityManager em) {
        this.em = em;
    }

    public void placeOrder(
        User user, 
        Long vendorId, 
        List<MenuItem> menuItems, 
        String customNote
    ) {

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            OrderRepository orderRepo = new OrderRepository(em);
            OrderItemRepository itemRepo = new OrderItemRepository(em);

            if (LocalTime.now().isAfter(LocalTime.of(9, 0))) {
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
                        com.etz.foodapp.vendor.Vendor.class, 
                        vendorId
                    ),
                    customNote, 
                    OrderSource.EMPLOYEE
            );

            orderRepo.save(order);

            for (MenuItem menuItem: menuItems) {
                OrderItem oi = new OrderItem(order, menuItem, 1);
                itemRepo.save(oi);
            }

            tx.commit();

        } catch (Exception e) {
            if(tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
