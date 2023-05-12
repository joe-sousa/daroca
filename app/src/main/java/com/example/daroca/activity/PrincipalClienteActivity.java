package com.example.daroca.activity;

import static com.example.daroca.R.id.recyclerViewPostProduto;
import static com.example.daroca.R.id.recyclerViewProdutosCliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.daroca.R;
import com.example.daroca.adapter.ProdutoAdapter;
import com.example.daroca.adapter.ProdutoAdapterCliente;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.Base64Custom;
import com.example.daroca.helper.RecyclerTouchListener;
import com.example.daroca.helper.UsuarioFirebase;
import com.example.daroca.model.Cliente;
import com.example.daroca.model.Produto;
import com.example.daroca.model.Produtor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalClienteActivity extends AppCompatActivity {
    private TextView saudacao;

    private FirebaseAuth auth = ConfiguracaoAuthFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();
    private DatabaseReference produtoRef;

    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerProduto;
    private RecyclerView recyclerViewProduto;
    private List<Produto> produtos = new ArrayList<>();
    ProdutoAdapterCliente produtoAdapterCliente;
    private List<String> listaUserId;
    String nomeCliente;

    public static int posicaoProduto;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_buy){
            Intent intent = new Intent(getApplicationContext(), PedidoDaSacolaActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_cliente);
        saudacao = findViewById(R.id.textViewSaudacaoCliente);

        recuperarNomeCliente();
        setTitle("Produtos");

        recyclerViewProduto = findViewById(recyclerViewProdutosCliente);
        this.listarProdutos();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewProduto.setLayoutManager(layoutManager);
        produtoAdapterCliente = new ProdutoAdapterCliente(produtos);
        recyclerViewProduto.setAdapter(produtoAdapterCliente);
        recyclerViewProduto.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerViewProduto,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        posicaoProduto = position;
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    private void recuperarNomeCliente() {
        String idUser = UsuarioFirebase.getIdentificadorUsuario();
        usuarioRef = firebaseRef.child("usuario")
                .child("cliente")
                .child(idUser);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cliente cliente = snapshot.getValue(Cliente.class);
                nomeCliente = cliente.getNome();
                saudacao.setText("Olá " + nomeCliente);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void listarProdutos() {
         usuarioRef = firebaseRef.child("usuario")
                .child("produtor");

         usuarioRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for(DataSnapshot dados: snapshot.getChildren()){
                     Produtor produtor = dados.getValue(Produtor.class);
                     String idUser = produtor.getIdUsuario();

                     produtoRef = firebaseRef.child("produto")
                             .child(idUser);

                     valueEventListenerProduto = produtoRef.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             for(DataSnapshot dados: snapshot.getChildren()){
                                 Produto produto = dados.getValue(Produto.class);
                                 produtos.add(produto);
                             }
                             produtoAdapterCliente.notifyDataSetChanged();
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