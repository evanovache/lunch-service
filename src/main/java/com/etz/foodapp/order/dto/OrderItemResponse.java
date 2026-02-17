package com.etz.foodapp.order.dto;

import java.math.BigDecimal;

public class OrderItemResponse {
    
    private String itemName;
    private int quantity;
    private BigDecimal price;

    public OrderItemResponse(String itemName, int quantity, BigDecimal price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
