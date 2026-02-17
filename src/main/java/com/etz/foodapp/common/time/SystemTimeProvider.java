package com.etz.foodapp.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SystemTimeProvider implements TimeProvider {
    
    @Override 
    public LocalDate currentDate() {
        return LocalDate.now();
    }

    @Override 
    public LocalTime currentTime() {
        return LocalTime.now();
    }

    @Override 
    public LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }
}
