package com.example.daroca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daroca.R;
import com.example.daroca.adapter.PedidoAdapter;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.UsuarioFirebase;
import com.example.daroca.model.ItemPedido;
import com.example.daroca.model.Produtor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PedidoDaSacolaActivity extends AppCompatActivity {
    private RecyclerView recyclerViewItensDoPedido1;
    List<ItemPedido> listaPedidosSacola1 = new ArrayList<>();
    PedidoAdapter pedidoAdapter1;
    ItemPedido itemPedido;
    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();
    private DatabaseReference pedidoRef1;
    private DatabaseReference produtorRef;
    private ValueEventListener valueEventListenerItemPedido1;
    private ValueEventListener valueEventListenerNomeProdutor;
    private Button criarNovo;
    private Button finalizarPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_da_sacola);

        recyclerViewItensDoPedido1 = findViewById(R.id.idListaDaSacola);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewItensDoPedido1.setLayoutManager(layoutManager);
        pedidoAdapter1 = new PedidoAdapter(listaPedidosSacola1);
        recyclerViewItensDoPedido1.setAdapter(pedidoAdapter1);
        criarNovo = findViewById(R.id.buttonNovo);
        finalizarPedido = findViewById(R.id.buttonConcluir);

        recyclerViewItensDoPedido1.setHasFixedSize(true);
        recyclerViewItensDoPedido1.addItemDecoration(new DividerItemDecoration(this,
                LinearLayout.VERTICAL));

        criarNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PrincipalClienteActivity.class);
                startActivity(intent);
            }
        });

        finalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PrincipalClienteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        listarPedidos();
    }

    public void listarPedidos(){
        pedidoRef1 = firebaseRef.child("pedido")
                .child(UsuarioFirebase.getIdentificadorUsuario())
                .child("itemPedido");

        produtorRef = firebaseRef.child("usuario")
                .child("produtor");

        //Localiza todos os podutores cadastrados no nó para conseguir seus nomes
        valueEventListenerNomeProdutor = produtorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dados: snapshot.getChildren()) {
                    Produtor produtor = dados.getValue(Produtor.class);
                    //Localiza pedidos por nome do produtor
                    valueEventListenerItemPedido1 = pedidoRef1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dados: snapshot.getChildren()) {
                                ItemPedido itemPedido = dados.getValue(ItemPedido.class);
                                if(itemPedido.getNomeProdutor().equals(produtor.getNome()))
                                    listaPedidosSacola1.add(itemPedido);
                            }
                            pedidoAdapter1.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}