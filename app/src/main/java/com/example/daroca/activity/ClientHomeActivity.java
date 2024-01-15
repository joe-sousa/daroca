package com.example.daroca.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.daroca.R;
import com.example.daroca.adapter.ProductCategoryAdapter;
import com.example.daroca.model.ProductCategory;
import com.example.daroca.model.ProductCategoryItem;

import java.util.ArrayList;
import java.util.List;

public class ClientHomeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_client_home);

    initProductCategoriesRecyclerView();
  }

  private void initProductCategoriesRecyclerView() {
    RecyclerView productCategoriesRecyclerView = findViewById(R.id.productCategoriesRecyclerView);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    ProductCategoryAdapter productCategoryAdapter = new ProductCategoryAdapter(
            this.getProductCategories(),
            item -> Toast.makeText(this, item.getName(), Toast.LENGTH_LONG).show()
    );

    productCategoriesRecyclerView.setLayoutManager(layoutManager);
    productCategoriesRecyclerView.setAdapter(productCategoryAdapter);
  }

  private List<ProductCategory> getProductCategories() {
    List<ProductCategory> productCategories = new ArrayList<>();

    productCategories.add(new ProductCategory("Categoria 1", this.getProductCategoryItems()));
    productCategories.add(new ProductCategory("Categoria 2", this.getProductCategoryItems()));
    productCategories.add(new ProductCategory("Categoria 3", this.getProductCategoryItems()));
    productCategories.add(new ProductCategory("Categoria 4", this.getProductCategoryItems()));
    productCategories.add(new ProductCategory("Categoria 5", this.getProductCategoryItems()));
    productCategories.add(new ProductCategory("Categoria 6", this.getProductCategoryItems()));
    productCategories.add(new ProductCategory("Categoria 7", this.getProductCategoryItems()));
    productCategories.add(new ProductCategory("Categoria 8", this.getProductCategoryItems()));

    return productCategories;
  }

  private List<ProductCategoryItem> getProductCategoryItems() {
    List<ProductCategoryItem> productCategoryItems = new ArrayList<>();

    productCategoryItems.add(new ProductCategoryItem("Produto 1"));
    productCategoryItems.add(new ProductCategoryItem("Produto 2"));
    productCategoryItems.add(new ProductCategoryItem("Produto 3"));
    productCategoryItems.add(new ProductCategoryItem("Produto 4"));
    productCategoryItems.add(new ProductCategoryItem("Produto 5"));
    productCategoryItems.add(new ProductCategoryItem("Produto 6"));

    return productCategoryItems;
  }
}