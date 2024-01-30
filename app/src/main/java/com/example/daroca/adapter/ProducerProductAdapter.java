package com.example.daroca.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.daroca.R;
import com.example.daroca.helper.CurrencyFormatter;
import com.example.daroca.model.Produto;

import java.util.List;

public class ProducerProductAdapter extends RecyclerView.Adapter<ProducerProductAdapter.ProductCategoryItemViewHolder> {
  private final List<Produto> productCategoryItems;
  private final OnItemClickListener onItemClickListener;

  public ProducerProductAdapter(List<Produto> productCategoryItems, OnItemClickListener listener) {
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
    Produto productCategoryItem = this.productCategoryItems.get(position);
    holder.bind(productCategoryItem, this.onItemClickListener);
  }

  @Override
  public int getItemCount() {
    return this.productCategoryItems.size();
  }

  public interface OnItemClickListener {
    void onItemClick(Produto item);
  }

  static class ProductCategoryItemViewHolder extends RecyclerView.ViewHolder {
    public ProductCategoryItemViewHolder(@NonNull View itemView) {
      super(itemView);
    }

    public void bind(Produto productCategoryItem, OnItemClickListener onItemClickListener) {
      TextView productCategoryItemNameTextView = this.itemView.findViewById(R.id.productCategoryItemNameTextView);
      ImageView productCategoryItemImageView = this.itemView.findViewById(R.id.productCategoryItemImageView);
      TextView productCategoryItemUnitsTextView = this.itemView.findViewById(R.id.productCategoryItemUnitsTextView);
      TextView productCategoryItemPriceTextView = this.itemView.findViewById(R.id.productCategoryItemPriceTextView);

      productCategoryItemNameTextView.setText(productCategoryItem.getNome());
      productCategoryItemUnitsTextView.setText(productCategoryItem.getQuantidade() + " unidades");

      String formattedProductPrice = CurrencyFormatter.formatCurrency(productCategoryItem.getPreco());
      productCategoryItemPriceTextView.setText(formattedProductPrice);

      Glide.with(productCategoryItemImageView.getContext()).load(productCategoryItem.getFoto()).into(productCategoryItemImageView);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onItemClickListener.onItemClick(productCategoryItem);
        }
      });
    }
  }
}
