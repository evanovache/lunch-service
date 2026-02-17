package com.etz.foodapp.order;

import java.math.BigDecimal;

import com.etz.foodapp.menu.MenuItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @ManyToOne(optional = false) 
    @JoinColumn(name = "order_id") 
    private Order order;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private BigDecimal priceAtOrderTime;

    @Column(nullable = false) 
    private int quantity;

    protected OrderItem() {}

    public OrderItem(Order order, MenuItem menuItem, int quantity) {
        this.order = order;
        this.itemName = menuItem.getName();
        this.priceAtOrderTime = menuItem.getPrice();
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public BigDecimal getPriceAtOrderTime() {
        return priceAtOrderTime;
    }

    public int getQuantity() {
        return quantity;
    }
}