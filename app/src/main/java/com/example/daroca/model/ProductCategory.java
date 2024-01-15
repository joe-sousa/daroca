package com.example.daroca.model;

import java.util.List;

public class ProductCategory {
    private String name;
    private List<ProductCategoryItem> items;

    public ProductCategory(String name, List<ProductCategoryItem> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductCategoryItem> getItems() {
        return items;
    }

    public void setItems(List<ProductCategoryItem> items) {
        this.items = items;
    }
}
