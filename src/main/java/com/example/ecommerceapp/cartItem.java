package com.example.ecommerceapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class cartItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String productId;
    private String productName;
    private double price;
    private int quantity;
    private String imageUrl;

    public cartItem(String productId, String productName, double price,
                    int quantity, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
