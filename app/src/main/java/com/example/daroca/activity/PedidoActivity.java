package com.example.daroca.activity;

import static com.example.daroca.R.id.idRecyclerViewListaItensPedido;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daroca.R;
import com.example.daroca.adapter.PedidoAdapter;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.RecyclerTouchListener;
import com.example.daroca.helper.UsuarioFirebase;
import com.example.daroca.model.ItemPedido;
import com.example.daroca.model.Pedido;
import com.example.daroca.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PedidoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewItensDoPedido;
    List<ItemPedido> listaPedidos = new ArrayList<>();
    PedidoAdapter pedidoAdapter;
    ItemPedido itemPedido;
    private Button novoItem;
    private Button concluirPedido;
    private TextView totalPedido;

    Pedido pedido;
    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();
    private DatabaseReference pedidoRef;
    private ValueEventListener valueEventListenerNomeProdutor;
    private DatabaseReference produtorRef;
    private ValueEventListener valueEventListenerItemPedido;
    private DatabaseReference pedidoRef2;

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
        pedido = new Pedido();

        totalPedido = findViewById(R.id.textViewValorTotal);
        pedidoRef2 = ConfiguracaoAuthFirebase.getFirebaseDatabase()
                .child("pedido")
                .child(UsuarioFirebase.getIdentificadorUsuario());
        // Recuperar valor total do pedido do Firebase
        pedidoRef2.child("valorTotal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Double valorTotal = snapshot.getValue(Double.class);
                    Log.i("msg", "ValorTotalPedido " +valorTotal);
                    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                    String valorFormatado = decimalFormat.format(valorTotal);
                    totalPedido.setText("R$ " + valorFormatado);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PedidoAdapter", "Erro ao recuperar valor total do pedido: " + error.getMessage());
            }
        });

        Bundle dados = getIntent().getExtras();
        //Recebendo dados de ItemPedidoActivity
        Produto produto = (Produto) dados.getSerializable("objeto2");
        int qtd = dados.getInt("quantidade");
        String idProdutor = dados.getString("idProdutor");

        itemPedido = new ItemPedido();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(qtd);
        itemPedido.setIdProdutor(idProdutor);
        itemPedido.setValorPrecoXQtdItem(produto.getPreco() * produto.getQuantidade());
        itemPedido.getProduto().getNome();
        itemPedido.getProduto().getPreco();
        itemPedido.getIdProdutor();

        novoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedido.salvarValorTotal();
                Intent intent = new Intent(getApplicationContext(), PrincipalClienteActivity.class);
                intent.putExtra("idProdutor2", itemPedido.getIdProdutor());
                startActivity(intent);
            }
        });

        concluirPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProdutoresActivity.class);
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
                        double total = 0;
                        listaPedidos.clear();
                        for(DataSnapshot dados: snapshot.getChildren()) {
                            ItemPedido itemPedido = dados.getValue(ItemPedido.class);
                            listaPedidos.add(itemPedido);
                            total += itemPedido.getValorPrecoXQtdItem(); // Adicionar o valor do item ao total
                            Log.i("msg", "Pedidos " + listaPedidos);
                        }
                        pedido.setValorTotal(total); // Atualizar o valor total do pedido
                        pedido.salvarValorTotal(); // Salvar o valor total no Firebase
                        pedidoAdapter.notifyDataSetChanged();
                    }
                    @Override

                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}