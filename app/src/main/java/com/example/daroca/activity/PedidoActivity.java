package com.example.daroca.activity;

import static com.example.daroca.R.id.buttonConcluirPedido;
import static com.example.daroca.R.id.idRecyclerViewListaItensPedido;
import static com.example.daroca.R.id.recyclerViewProdutosCliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.daroca.R;
import com.example.daroca.adapter.PedidoAdapter;
import com.example.daroca.adapter.ProdutoAdapterCliente;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.RecyclerTouchListener;
import com.example.daroca.helper.UsuarioFirebase;
import com.example.daroca.model.ItemPedido;
import com.example.daroca.model.Pedido;
import com.example.daroca.model.Produto;
import com.example.daroca.model.Produtor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PedidoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewItensDoPedido;
    List<ItemPedido> listaPedidos = new ArrayList<>();
    PedidoAdapter pedidoAdapter;
    ItemPedido itemPedido;
    private Button novoItem;
    private Button concluirPedido;

    Pedido pedido = new Pedido();
    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();
    private DatabaseReference pedidoRef;
    private ValueEventListener valueEventListenerNomeProdutor;
    private DatabaseReference produtorRef;
    private ValueEventListener valueEventListenerItemPedido;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        setTitle("Pedidos");

        recyclerViewItensDoPedido = findViewById(idRecyclerViewListaItensPedido);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewItensDoPedido.setLayoutManager(layoutManager);
        pedidoAdapter = new PedidoAdapter(listaPedidos);
        recyclerViewItensDoPedido.setHasFixedSize(true);
        recyclerViewItensDoPedido.addItemDecoration(new DividerItemDecoration(this,
                LinearLayout.VERTICAL));

        recyclerViewItensDoPedido.setAdapter(pedidoAdapter);
        novoItem = findViewById(R.id.buttonNovoPedido);
        concluirPedido = findViewById(R.id.buttonConcluirPedido);

        Bundle dados = getIntent().getExtras();
        //Recebendo dados de ItemPedidoActivity
        Produto produto = (Produto) dados.getSerializable("objeto");
        int qtd = dados.getInt("quantidade");
        String nomeProdutor = dados.getString("nomeProdutor");

        itemPedido = new ItemPedido();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(qtd);
        itemPedido.setNomeProdutor(nomeProdutor);
        itemPedido.getProduto().getNome();
        itemPedido.getProduto().getPreco();
        itemPedido.getNomeProdutor();

        //Calculando o valor total do pedido
        //calcularTotal();
        //Log.i("valorTotal", "valorPedido" + calcularTotal());

        novoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PrincipalClienteActivity.class);
                startActivity(intent);
            }
        });

        concluirPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PrincipalClienteActivity.class);
                startActivity(intent);
            }
        });

        recyclerViewItensDoPedido.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerViewItensDoPedido,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        ItemPedido itemPedido = listaPedidos.get(position);
                        Toast.makeText(PedidoActivity.this,
                                "Item pressionado" + itemPedido.getProduto().getNome(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        ItemPedido itemPedido = listaPedidos.get(position);
                        Toast.makeText(PedidoActivity.this,
                                "Clique longo " + itemPedido.getProduto().getNome(),
                                Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        listarPedidos();
    }

    public void listarPedidos(){
        pedidoRef = firebaseRef.child("pedido")
                .child(UsuarioFirebase.getIdentificadorUsuario())
                .child("itemPedido");

                valueEventListenerItemPedido = pedidoRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dados: snapshot.getChildren()) {
                            ItemPedido itemPedido = dados.getValue(ItemPedido.class);
                            listaPedidos.add(itemPedido);
                        }
                        pedidoAdapter.notifyDataSetChanged();
                    }
                    @Override

                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}