package com.example.daroca.adapter;
import static com.example.daroca.activity.ProdutoresActivity.posicaoProdutor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
import com.example.daroca.activity.PrincipalClienteActivity;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.UsuarioFirebase;
import com.example.daroca.model.Produto;
import com.example.daroca.model.Produtor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProdutorAdapter extends RecyclerView.Adapter<ProdutorAdapter.MyViewHolder> implements View.OnClickListener{
    private List<Produtor> produtores;
    private Context context;
    Produtor produtor;
    Produto produto;

    DatabaseReference firebase = ConfiguracaoAuthFirebase.getFirebaseDatabase();
    DatabaseReference prodRef;
    DatabaseReference produtorRef;
    ValueEventListener prodEvent;

    public ProdutorAdapter(List<Produtor> listaProdutores) {
        this.produtores = listaProdutores;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_produtores_layout, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        produtor = produtores.get(position);
        String idProdutor = produtor.getIdUsuario();

        prodRef = firebase.child("produto")
                .child(idProdutor);
        prodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dados: snapshot.getChildren()){
                    Produto produto = dados.getValue(Produto.class);
                    holder.category.setText(produto.getCategoria());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.productorName.setText(produtor.getNome());
        Glide.with(holder.produtorImage).load(produtor.getFoto()).into(holder.produtorImage);
        holder.listProducts.setClickable(true);
        holder.listProducts.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return produtores.size();
    }

    @Override
    public void onClick(View v) {
        produtorRef = firebase.child("pedido");
        final Produtor produtor = produtores.get(posicaoProdutor);
        String idUsuarioProdutor = produtor.getIdUsuario();

        produtorRef.addValueEventListener(new ValueEventListener() {
            boolean correspondenciaEncontrada = false;  // Flag para rastrear correspondência
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot pedidoSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot itemPedidoSnapshot : pedidoSnapshot.child("itemPedido").getChildren()) {
                             String idProdutorPedido = itemPedidoSnapshot.child("idProdutor").getValue(String.class);
                            Log.d("MeuApp", "idProdutorPedido: " + idProdutorPedido);
                            if (idUsuarioProdutor.equals(idProdutorPedido) || idProdutorPedido.equals(null)) {
                                // O idProdutor do pedido corresponde ao idUsuario do produtor
                                correspondenciaEncontrada = true;
                                break;  // Correspondência encontrada, saia do loop
                            }
                        }
                    }
                }else {
                    correspondenciaEncontrada = true;
                    }


                if (correspondenciaEncontrada) {
                    // Listar produtos apenas se houver uma correspondência
                    Intent intent = new Intent(context, PrincipalClienteActivity.class);
                    intent.putExtra("produtorId", idUsuarioProdutor);
                    context.startActivity(intent);
                } else {
                    finalizarPedidoOuEsvaziarSacola();
                }
            }

            private void finalizarPedidoOuEsvaziarSacola() {
                // Não foi encontrada correspondência, mostrar um AlertDialog de aviso com opção para esvaziar a sacola
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Aviso");
                builder.setMessage("Favor finalize o pedido anterior ou esvazie a sacola");

                builder.setPositiveButton("Finalizar pedido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        produtorRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot pedidoSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot itemPedidoSnapshot : pedidoSnapshot.child("itemPedido").getChildren()) {
                                        String idProdutorPedido = itemPedidoSnapshot.child("idProdutor").getValue(String.class);
                                        Intent intent = new Intent(context, PrincipalClienteActivity.class);
                                        intent.putExtra("produtorId4", idProdutorPedido);
                                        context.startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                builder.setNegativeButton("Esvaziar sacola", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,
                                "Esvaziaremos a sacola",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView productorName;
        private TextView textTitleCategory;
        private TextView category;
        private ImageView produtorImage;
        private Button listProducts;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            productorName = itemView.findViewById(R.id.textTitleProductor);
            produtorImage = itemView.findViewById(R.id.productorPerfilImage);
            listProducts = itemView.findViewById(R.id.buttonListproducts);
            textTitleCategory = itemView.findViewById(R.id.textTitleCategory);
            category = itemView.findViewById(R.id.textCategory);
        }
    }
}
