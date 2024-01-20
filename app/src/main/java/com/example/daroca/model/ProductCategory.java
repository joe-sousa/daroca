package com.example.daroca.model;

import java.util.List;

public class ProductCategory {
    private String name;
    private List<Produto> items;

    public ProductCategory(String name, List<Produto> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Produto> getItems() {
        return items;
    }

    public void setItems(List<Produto> items) {
        this.items = items;
    }
}
