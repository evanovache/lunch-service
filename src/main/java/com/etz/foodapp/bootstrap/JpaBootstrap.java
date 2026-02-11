package com.etz.foodapp.bootstrap;

import java.time.LocalDate;

import com.etz.foodapp.common.jpa.EntityManagerProvider;
import com.etz.foodapp.order.repository.OrderRepository;

import jakarta.persistence.EntityManager;

public class JpaBootstrap {
    
    public static void main(String[] args) {
        
        EntityManager em = EntityManagerProvider.createEntityManager();

        OrderRepository orderRepo = new OrderRepository(em);

        orderRepo.findAllForDate(LocalDate.now())
                 .forEach(o -> System.out.println(o.getId()));
    }
}
