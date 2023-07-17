package com.example.freshly;

public class Auction {

    private String productId;

    private String remainingDays;

    private String price;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(String remainingDays) {
        this.remainingDays = remainingDays;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Auction(String productId, String remainingDays, String price) {
        this.productId = productId;
        this.remainingDays = remainingDays;
        this.price = price;
    }
}
