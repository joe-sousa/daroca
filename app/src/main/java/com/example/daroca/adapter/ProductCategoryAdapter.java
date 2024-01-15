package com.example.daroca.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daroca.R;
import com.example.daroca.model.ProductCategory;

import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ProductCategoryViewHolder> {

  private final List<ProductCategory> productCategories;
  private final RecyclerView.RecycledViewPool viewPool;
  private final ProductCategoryItemAdapter.OnItemClickListener onItemClickListener;

  public ProductCategoryAdapter(List<ProductCategory> productCategories, ProductCategoryItemAdapter.OnItemClickListener onItemClickListener) {
    this.productCategories = productCategories;

    this.onItemClickListener = onItemClickListener;
    this.viewPool = new RecyclerView.RecycledViewPool();
  }

  @NonNull
  @Override
  public ProductCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.product_category_layout,
            parent,
            false
    );

    return new ProductCategoryViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ProductCategoryViewHolder productCategoryViewHolder, int position) {
    ProductCategory productCategory = this.productCategories.get(position);
    productCategoryViewHolder.bind(productCategory);

    this.createProductItemsRecyclerView(productCategoryViewHolder, productCategory);
  }

  private void createProductItemsRecyclerView(
          @NonNull ProductCategoryViewHolder productCategoryViewHolder,
          ProductCategory productCategory
  ){
    RecyclerView productCategoryItemsRecyclerView = productCategoryViewHolder.getItemsRecyclerView();

    LinearLayoutManager layoutManager = new LinearLayoutManager(
            productCategoryItemsRecyclerView.getContext(),
            LinearLayoutManager.HORIZONTAL,
            false
    );
    layoutManager.setInitialPrefetchItemCount(productCategory.getItems().size());

    productCategoryItemsRecyclerView.setLayoutManager(layoutManager);

    ProductCategoryItemAdapter productCategoryItemAdapter = new ProductCategoryItemAdapter(productCategory.getItems(), this.onItemClickListener);
    productCategoryItemsRecyclerView.setAdapter(productCategoryItemAdapter);

    productCategoryItemsRecyclerView.setRecycledViewPool(this.viewPool);
  }

  @Override
  public int getItemCount() {
    return productCategories.size();
  }

  static class ProductCategoryViewHolder extends RecyclerView.ViewHolder {
    private RecyclerView itemsRecyclerView;

    public ProductCategoryViewHolder(View itemView) {
      super(itemView);
    }

    public void bind(ProductCategory productCategory) {
      TextView productCategoryNameTextView = this.itemView.findViewById(R.id.productCategoryNameTextView);
      productCategoryNameTextView.setText(productCategory.getName());

      this.itemsRecyclerView = this.itemView.findViewById(R.id.productCategoryItemsRecyclerView);
    }

    public RecyclerView getItemsRecyclerView() {
      return itemsRecyclerView;
    }
  }
}
