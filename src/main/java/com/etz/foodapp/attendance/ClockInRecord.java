package com.etz.foodapp.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.etz.foodapp.auth.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity 
@Table(
    name = "clock_in_records", 
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "clock_date"})
)
public class ClockInRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) 
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "clock_date", nullable = false) 
    private LocalDate date; 

    @Column(nullable = false) 
    private LocalDateTime clockInTime;

    protected ClockInRecord() {}

    public ClockInRecord(User user) {
        this.user = user;
        this.date = LocalDate.now();
        this.clockInTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getClockInTime() {
        return clockInTime;
    }
}
