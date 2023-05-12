package com.example.daroca.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daroca.R;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.model.ItemPedido;
import com.example.daroca.model.Produtor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.MyViewHolder> {

    private List<ItemPedido> listaItemPedidos;
    public PedidoAdapter(List<ItemPedido> listaPedidos)
    {
        this.listaItemPedidos = listaPedidos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pedidoLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pedido_layout, parent, false);
        return new MyViewHolder(pedidoLista);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemPedido itemPedido = listaItemPedidos.get(position);
        holder.textNome.setText(itemPedido.getProduto().getNome());
        holder.textPrice.setText(String.valueOf(itemPedido.getProduto().getPreco()));
        holder.textQuantity.setText(String.valueOf(itemPedido.getQuantidade()));
        holder.nomeProdutor.setText(itemPedido.getNomeProdutor());
        holder.textQuantityxPrecoResult.setText(String.valueOf(itemPedido.getValorPrecoXQtdItem()));
        holder.result.setText(String.valueOf(itemPedido.getValorTotal()));
        holder.nomeProduto.setText("Nome");
        holder.precoProduto.setText("Preço");
        holder.qtdProduto.setText("Quantidade");
        holder.textProdutor.setText("Nome do produtor");
        holder.textQuantityxPreco.setText("Total");
    }

    @Override
    public int getItemCount() {
        return listaItemPedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nomeProduto;
        private TextView textProdutor;
        private TextView precoProduto;
        private TextView qtdProduto;
        private TextView textNome;
        private TextView textPrice;
        private TextView textQuantity;
        private TextView nomeProdutor;
        private TextView textQuantityxPreco;
        private TextView textQuantityxPrecoResult;
        private TextView result;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeProduto = itemView.findViewById(R.id.textViewNomeProduto);
            precoProduto = itemView.findViewById(R.id.textViewPrecoProduto);
            qtdProduto = itemView.findViewById(R.id.textViewQuantidadePedido);
            textNome = itemView.findViewById(R.id.textTitulo);
            textPrice = itemView.findViewById(R.id.textPreco);
            textQuantity = itemView.findViewById(R.id.textQuantidadePedido);
            textProdutor = itemView.findViewById(R.id.textViewTitleNomeProdutor);
            nomeProdutor = itemView.findViewById(R.id.textViewNomeProdutor);
            textQuantityxPreco = itemView.findViewById(R.id.textViewQuantidadexPreco);
            textQuantityxPrecoResult = itemView.findViewById(R.id.textViewQuantidadexPrecoResult);
            result = itemView.findViewById(R.id.textViewFinalResult);
        }
    }
}
