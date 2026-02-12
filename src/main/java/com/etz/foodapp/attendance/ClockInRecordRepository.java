package com.etz.foodapp.attendance;

import java.time.LocalDate;
import java.util.Optional;

import jakarta.persistence.EntityManager;

public class ClockInRecordRepository {
    
    private final EntityManager em;

    public ClockInRecordRepository(EntityManager em) {
        this.em = em;
    }

    public Optional<ClockInRecord> findByUserAndDate(Long userId, LocalDate date) {

        var results = em.createQuery(
            """
            SELECT c 
            FROM ClockInRecord c 
            WHERE c.user.id = :userId 
                AND c.date = :date
            """,
            ClockInRecord.class
        )
        .setParameter("userId", userId)
        .setParameter("date", date) 
        .getResultList();

        return results.stream().findFirst();
    }
}
