package com.example.ecommerceapp;

import java.util.List;

public class order {
    private String orderId;
    private List<cartItem> items;
    private double subtotal;
    private double tax;
    private double shipping;
    private double total;
    private String customerName;
    private String customerEmail;
    private String shippingAddress;
    private String paymentMethod;
    private long orderDate;
    private String status;

    public order(String orderId, List<cartItem> items, double subtotal, double tax,
                 double shipping, double total, String customerName, String customerEmail,
                 String shippingAddress, String paymentMethod, long orderDate, String status) {
        this.orderId = orderId;
        this.items = items;
        this.subtotal = subtotal;
        this.tax = tax;
        this.shipping = shipping;
        this.total = total;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public List<cartItem> getItems() { return items; }
    public void setItems(List<cartItem> items) { this.items = items; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }
    public double getShipping() { return shipping; }
    public void setShipping(double shipping) { this.shipping = shipping; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public long getOrderDate() { return orderDate; }
    public void setOrderDate(long orderDate) { this.orderDate = orderDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
