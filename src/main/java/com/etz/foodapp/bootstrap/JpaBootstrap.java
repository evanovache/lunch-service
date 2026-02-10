package com.etz.foodapp.bootstrap;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.etz.foodapp.auth.Role;
import com.etz.foodapp.auth.User;
import com.etz.foodapp.common.jpa.EntityManagerProvider;
import com.etz.foodapp.menu.Menu;
import com.etz.foodapp.menu.MenuItem;
import com.etz.foodapp.order.Order;
import com.etz.foodapp.order.OrderItem;
import com.etz.foodapp.order.OrderSource;
import com.etz.foodapp.vendor.Vendor;

import jakarta.persistence.EntityManager;

public class JpaBootstrap {
    
    public static void main(String[] args) {
        
        EntityManager em = 
                EntityManagerProvider.createEntityManager();

        em.getTransaction().begin();

        Vendor vendor = new Vendor("Ama's Kitchen");
        em.persist(vendor);
        
        User employee = new User("Evans", "evans@mail.com", Role.EMPLOYEE);

        Order order = new Order(
                employee,
                vendor, 
                "Extra pepper please", 
                OrderSource.EMPLOYEE
        );
        em.persist(order);

        Menu menu = new Menu(
                vendor, 
                LocalDate.now(), 
                LocalDate.now()
        );

        MenuItem item1 = new MenuItem(menu, "Jollof Rice", new BigDecimal("20.00"));
        OrderItem oi = new OrderItem(order, item1, 1);

        em.getTransaction().commit();

        em.persist(order);
        em.persist(oi);
        em.close();
        EntityManagerProvider.shutdown();
    }
}
