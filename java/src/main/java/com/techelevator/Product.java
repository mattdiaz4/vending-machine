package com.techelevator;

import java.math.BigDecimal;

public class Product {

    private String slotLocation;
    private String name;
    private BigDecimal price;
    private String category;
    private int quantity;


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(String slotLocation, String name, BigDecimal price, String category, int quantity) {
        this.slotLocation = slotLocation;
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;

    }

    public String getSlotLocation() {
        return slotLocation;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }


    }

