package com.etz.foodapp.menu;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "menu_items")
public class MenuItem {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)  
    @JoinColumn(name = "menu_id") 
    private Menu menu;

    @Column(nullable = false) 
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false) 
    private boolean available = true;

    protected MenuItem() {}

    public MenuItem(Menu menu, String name, BigDecimal price) {
        this.menu = menu; 
        this.name = name; 
        this.price = price; 
        this.available = true;
    }

    public Long getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markUnavailable() {
        this.available = false;
    }
}
