package com.etz.foodapp.order.dto;

public class OrderItemRequest {
    
    private Long menuItemId;
    private int quantity;

    public OrderItemRequest() {}

    public Long getMenuItemId() {
        return menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }
}
