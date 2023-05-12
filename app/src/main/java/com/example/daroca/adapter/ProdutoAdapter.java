package com.example.daroca.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.daroca.R;
import com.example.daroca.model.Produto;

import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.MyViewHolder> implements View.OnClickListener {

    private List<Produto> produtos;

    public ProdutoAdapter(List<Produto> anuncioProdutos) {
        this.produtos = anuncioProdutos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_layout, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produto produto = produtos.get(position);
        holder.textTitle.setText(produto.getNome());
        holder.textPrice.setText(String.valueOf(produto.getPreco()));
        holder.textQuantity.setText(String.valueOf(produto.getQuantidade()));
        holder.textDescription.setText(produto.getDescricao());
        Glide.with(holder.imageProduct).load(produto.getFoto()).into(holder.imageProduct);
        holder.like.setText(String.valueOf(produto.getCurtidas()));
        holder.editar.setClickable(true);
        holder.editar.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(),
                "Em breve disponibilizaremos este recurso",
                Toast.LENGTH_SHORT).show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textTitle;
        private TextView textDescription;
        private TextView textPrice;
        private TextView textQuantity;
        private ImageView imageProduct;
        private Button like;
        private Button editar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitlePost);
            textDescription = itemView.findViewById(R.id.textDescricao);
            textPrice = itemView.findViewById(R.id.textPreco);
            textQuantity = itemView.findViewById(R.id.textQtdProdutoLayout);
            imageProduct = itemView.findViewById(R.id.imagePost);
            like = itemView.findViewById(R.id.buttonLikeProdutoLayout);
            editar = itemView.findViewById(R.id.buttonEditProdutoLayout);
        }
    }

}
