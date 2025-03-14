package com.example.megaCabService.models;

import java.math.BigDecimal;

public class CategoryModel {
    private int categoryID;
    private String category;
    private BigDecimal price;
    private BigDecimal driverCost;

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDriverCost() { 
        return driverCost;
    }

    public void setDriverCost(BigDecimal driverCost) {
        this.driverCost = driverCost;
    }
}
