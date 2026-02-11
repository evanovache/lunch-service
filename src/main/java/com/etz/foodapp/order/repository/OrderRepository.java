package com.etz.foodapp.order.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.etz.foodapp.order.Order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class OrderRepository {
    
    private final EntityManager em;

    public OrderRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Order order) {
        em.persist(order);
    }


    public Optional<Order> findByUserAndVendorAndDate(
            Long userId, 
            Long vendorId, 
            LocalDate date
    ) {
        TypedQuery<Order> query = em.createQuery(
            """
            SELECT o 
            FROM Order o 
            WHERE o.user.id = :userId 
                AND o.vendor.id = :vendorId
                AND o.orderDate = :date
            """,
            Order.class
        );

        query.setParameter("userId", userId);
        query.setParameter("vendorId", vendorId);
        query.setParameter("date", date);

        List<Order> results = query.getResultList();

        return results.stream().findFirst();
    }


    public List<Order> findAllForDate(LocalDate date) {
        TypedQuery<Order> query = em.createQuery(
            """
            SELECT o 
            FROM Order o 
            WHERE o.orderDate = :date 
            """, 
            Order.class
        );
        query.setParameter("date", date);
        return query.getResultList();
    }

    
    public List<Order> findAllForVendorAndDate(Long vendorId, LocalDate date) {
        TypedQuery<Order> query = em.createQuery(
            """
            SELECT o 
            FROM Order o
            WHERE o.vendor.id = :vendorId 
                AND o.orderDate = :date    
            """,
            Order.class
        );
        query.setParameter("vendorId", vendorId);
        query.setParameter("date", date);
        return query.getResultList();
    }
}
