package com.etz.foodapp.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    
    private Long orderId;
    private String vendorName;
    private LocalDateTime orderTime;
    private List<OrderItemResponse> items;
    private String customNote;

    public OrderResponse (
            Long orderId, 
            String vendorName, 
            LocalDateTime orderTime, 
            List<OrderItemResponse> items, 
            String customNote
    ) {
        this.orderId = orderId;
        this.vendorName = vendorName;
        this.orderTime = orderTime;
        this.items = items;
        this.customNote = customNote;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public String getCustomNote() {
        return customNote;
    }
}
