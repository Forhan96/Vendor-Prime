package com.shadigipay.shaprimevendor;

public class OrderData {

    private String vendorId, orderId, price, commission;

    OrderData(String vendorId, String orderId, String price, String commission) {
        this.vendorId = vendorId;
        this.orderId = orderId;
        this.price = price;
        this.commission = commission;

    }

    String getOrderId() {
        return orderId;
    }

    String getVendorId() {
        return vendorId;
    }

    String getPrice() {
        return price;
    }

    String getCommission() {
        return commission;
    }
}
