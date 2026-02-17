package com.etz.foodapp.order.dto;

import java.util.List;

public class OrderRequest {
    
    private Long vendorId;
    private List<OrderItemRequest> items;
    private String customNote;

    public OrderRequest() {}

    public Long getVendorId() {
        return vendorId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public String getCustomNote() {
        return customNote;
    }
}
