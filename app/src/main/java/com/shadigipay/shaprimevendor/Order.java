package com.shadigipay.shaprimevendor;

public class Order {
    private String orderId, vendorId;

    Order(String orderId, String vendorId) {
        this.orderId = orderId;
        this.vendorId = vendorId;
    }

    String getOrderId() {
        return orderId;
    }

    String getVendorId() {
        return vendorId;
    }
}

