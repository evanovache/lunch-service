package com.etz.foodapp.order;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.etz.foodapp.auth.User;
import com.etz.foodapp.vendor.Vendor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity 
@Table(
    name = "orders", 
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "vendor_id", "order_date"}
    )
)
public class Order {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 

    @ManyToOne(optional = false) 
    @JoinColumn(name = "user_id") 
    private User user;

    @ManyToOne(optional = false) 
    @JoinColumn(name = "vendor_id") 
    private Vendor vendor;

    @Column(name = "order_date", nullable = false) 
    private LocalDate orderDate;

    @Column(nullable = false) 
    private LocalDateTime orderTime;
    
    @Column(length = 255) 
    private String customNote;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false) 
    private OrderSource source;

    protected Order() {}

    public Order(User user, 
                 Vendor vendor, 
                 String customNote, 
                 OrderSource source,
                 LocalDate date,
                 LocalDateTime dateTime) {
        this.user = user; 
        this.vendor = vendor;
        this.customNote = customNote;
        this.source = source; 
        this.orderDate = date;
        this.orderTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getCustomNote() {
        return customNote;
    }

    public OrderSource getSource() {
        return source;
    }
}
