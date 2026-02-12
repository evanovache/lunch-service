package com.etz.foodapp.common.jpa;

import java.util.function.Consumer;
import java.util.function.Function;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TransactionExecutor {
    
    private final EntityManager em;

    public TransactionExecutor(EntityManager em) {
        this.em = em;
    }

    public <T> T execute(Function<EntityManager, T> work) {

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            T result = work.apply(em);

            tx.commit();

            return result;

        } catch (RuntimeException e) {

            if(tx.isActive()) {
                tx.rollback();
            }
            
            throw e;
        }
    }

    public void executeVoid(Consumer<EntityManager> work) {
        execute(em -> {
            work.accept(em);
            return null;
        });
    }
}
