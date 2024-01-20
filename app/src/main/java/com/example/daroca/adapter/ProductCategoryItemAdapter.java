package com.example.daroca.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.daroca.R;
import com.example.daroca.model.ProductCategoryItem;

import java.util.List;

public class ProductCategoryItemAdapter extends RecyclerView.Adapter<ProductCategoryItemAdapter.ProductCategoryItemViewHolder> {
  private final List<ProductCategoryItem> productCategoryItems;
  private final OnItemClickListener onItemClickListener;

  public ProductCategoryItemAdapter(List<ProductCategoryItem> productCategoryItems, OnItemClickListener listener) {
    this.productCategoryItems = productCategoryItems;
    this.onItemClickListener = listener;
  }

  @NonNull
  @Override
  public ProductCategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.product_category_item_layout,
            parent,
            false
    );

    return new ProductCategoryItemViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ProductCategoryItemViewHolder holder, int position) {
    ProductCategoryItem productCategoryItem = this.productCategoryItems.get(position);
    holder.bind(productCategoryItem, this.onItemClickListener);
  }

  @Override
  public int getItemCount() {
    return this.productCategoryItems.size();
  }

  public interface OnItemClickListener {
    void onItemClick(ProductCategoryItem item);
  }

  static class ProductCategoryItemViewHolder extends RecyclerView.ViewHolder {
    public ProductCategoryItemViewHolder(@NonNull View itemView) {
      super(itemView);
    }

    public void bind(ProductCategoryItem productCategoryItem, OnItemClickListener onItemClickListener) {
      TextView productCategoryItemNameTextView = this.itemView.findViewById(R.id.productCategoryItemNameTextView);
      ImageView productCategoryItemImageView = this.itemView.findViewById(R.id.productCategoryItemImageView);

      productCategoryItemNameTextView.setText(productCategoryItem.getName());
      String url = productCategoryItem.getImage();
      Glide.with(productCategoryItemImageView.getContext()).load(url).into(productCategoryItemImageView);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onItemClickListener.onItemClick(productCategoryItem);
        }
      });
    }
  }
}
