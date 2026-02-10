package com.etz.foodapp.common.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerProvider {
    
    private static final EntityManagerFactory emf = 
            Persistence.createEntityManagerFactory("play");
    
    private EntityManagerProvider() {}

    public static EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public static void shutdown() {
        emf.close();
    }
}
