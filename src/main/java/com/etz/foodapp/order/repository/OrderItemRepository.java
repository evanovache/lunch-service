package com.etz.foodapp.order.repository;

import java.time.LocalDate;
import java.util.List;

import com.etz.foodapp.order.OrderItem;

import jakarta.persistence.EntityManager;

public class OrderItemRepository {
    
    private final EntityManager em;

    public OrderItemRepository(EntityManager em) {
        this.em = em;
    }

    public void save(OrderItem orderItem) {
        em.persist(orderItem);
    }

    public List<OrderItem> findAllForVendorAndDate(Long vendorId, LocalDate date) {

        return em.createQuery( 
            """
            SELECT oi 
            FROM OrderItem oi 
            WHERE oi.order.vendor.id = :vendorId
                AND oi.order.orderDate = :date
            """, 
            OrderItem.class
        )
        .setParameter("vendorId", vendorId)
        .setParameter("date", date) 
        .getResultList();
    }
}
