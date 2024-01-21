package com.example.daroca.activity;

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
import android.view.View;
import android.widget.TextView;

import com.example.daroca.R;
import com.example.daroca.adapter.ProdutoAdapterCliente;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.RecyclerTouchListener;
import com.example.daroca.helper.UsuarioFirebase;
import com.example.daroca.model.Cliente;
import com.example.daroca.model.Produto;
import com.example.daroca.model.Produtor;
import com.example.daroca.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalComercianteActivity extends AppCompatActivity {

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
    Usuario produtor;

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

    //TODO SuppressLint é realmente necessario ?
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_comerciante);

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
        produtor = new Produtor();
        Bundle dados = getIntent().getExtras();

        /*Recebendo id do produtor através do Bundle e ProdutorAdapter*/
        String idProdutor = dados.getString("produtorId");
        Log.i("msg", "idProdutor" + idProdutor);

        /*Recebendo id do produtor através do Bundle e ProdutorAdapter quando
         optou por não esvaizar sacola e deseja finalizar pedido*/
        String idProdutor4 = dados.getString("produtorId4");
        Log.i("msg", "idProdutor4" + idProdutor4);

        /*Recebendo id do produtor através do Bundle e do PedidoActivity*/
        String idProdutor2 = dados.getString("idProdutor2");
        Log.i("msg", "idProdutor2" + idProdutor2);

        /*Recebendo id do produtor através do Bundle e do PedidoDaSacolaActivity*/
        String idProdutor3 = dados.getString("idProdutor3");
        Log.i("msg", "idProdutor3" + idProdutor3);

        /*Lógica que define de onde o Id do produtor virá, se do ProdutorAdapter ou
         * do PedidoActivity para poder gerar lista de produtos por produtor*/
        if(idProdutor != null){
            produtoRef = firebaseRef.child("produto")
                    .child(idProdutor);
        }else if(idProdutor2 != null){
            produtoRef = firebaseRef.child("produto")
                    .child(idProdutor2);
        }else if(idProdutor3 != null){
            produtoRef = firebaseRef.child("produto")
                    .child(idProdutor3);
        }else if(idProdutor4 != null) {
            produtoRef = firebaseRef.child("produto")
                    .child(idProdutor4);
        }

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