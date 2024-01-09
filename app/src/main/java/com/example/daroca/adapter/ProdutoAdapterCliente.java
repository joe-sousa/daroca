package com.example.daroca.adapter;

import static com.example.daroca.activity.PrincipalClienteActivity.posicaoProduto;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.daroca.R;
import com.example.daroca.activity.ItemPedidoActivity;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.model.ItemPedido;
import com.example.daroca.model.Produto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapterCliente extends RecyclerView.Adapter<ProdutoAdapterCliente.MyViewHolder> implements View.OnClickListener {

    private List<Produto> produtos;
    private Context context;
    Produto produto;
    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();

    private DatabaseReference pedidoRef;

    ValueEventListener pedidoEventListener;

    ItemPedido itemPedido;

    public ProdutoAdapterCliente(List<Produto> vitrineProdutos) {
        this.produtos = vitrineProdutos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.produto_para_cliente_layout, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        produto = produtos.get(position);
        holder.textTitle.setText(produto.getNome());
        holder.textPrice.setText(String.valueOf(produto.getPreco()));
        holder.textQuantity.setText(String.valueOf(produto.getQuantidade()));
        holder.textDescription.setText(produto.getDescricao());
        Glide.with(holder.imageProduct).load(produto.getFoto()).into(holder.imageProduct);
        holder.like.setText(String.valueOf(produto.getCurtidas()));
        holder.buy.setClickable(true);
        holder.buy.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    @Override
    public void onClick(View v) {
            Produto produto = produtos.get(posicaoProduto);
            Intent intent = new Intent(context, ItemPedidoActivity.class);
            intent.putExtra("objeto1", produto);
            intent.putExtra("quantidade", produto.getQuantidade());
            context.startActivity(intent);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textDescription;
        private TextView textPrice;
        private TextView textQuantity;
        private ImageView imageProduct;
        private Button like;
        private Button buy;
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            textTitle = itemView.findViewById(R.id.textNomeProdutoLayout);
            textDescription = itemView.findViewById(R.id.textDescricaoProdutoClienteLayout);
            textPrice = itemView.findViewById(R.id.textPrecoProdutoLayout);
            textQuantity = itemView.findViewById(R.id.textQtdProdutoLayout);
            imageProduct = itemView.findViewById(R.id.imageProdutoLayout);
            like = itemView.findViewById(R.id.buttonLikeProdutoLayout);
            buy = itemView.findViewById(R.id.buttonEditProdutoLayout);
        }
    }
}
