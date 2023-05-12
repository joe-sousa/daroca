package com.example.daroca.activity;

import static com.example.daroca.R.id.recyclerViewPostProduto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daroca.R;
import com.example.daroca.adapter.ProdutoAdapter;
import com.example.daroca.cadastrarDAO.ProdutoDAO;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.databinding.ActivityMainBinding;
import com.example.daroca.helper.Base64Custom;
import com.example.daroca.model.Produto;
import com.example.daroca.model.Produtor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalProdutorActivity extends AppCompatActivity {
    private TextView saudacao;
    private FirebaseAuth auth = ConfiguracaoAuthFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;

    private DatabaseReference produtoRef;

    private ValueEventListener valueEventListenerProduto;

    private RecyclerView recyclerViewProduto;
    private List<Produto> produtos = new ArrayList<>();

    ProdutoAdapter postAdapter;

    FloatingActionButton fabCadastrarProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_produtor);

        setTitle("Produtos");

        saudacao = findViewById(R.id.textViewSaudacaoProdutor);
        recyclerViewProduto = findViewById(recyclerViewPostProduto);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerViewProduto.setLayoutManager(layoutManager);

        this.listarProdutos();

         postAdapter = new ProdutoAdapter(produtos);
        recyclerViewProduto.setAdapter(postAdapter);

        fabCadastrarProduto = findViewById(R.id.floatingActionButton);

        fabCadastrarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastrarProdutoActivity1.class);
                startActivity(intent);
            }
        });


    }

    /*public void cadastrarProduto(){
        Produto produto1 = new Produto();
        produto1.setNome("Banana");
        produto1.setDescricao("Banana prata");
        produto1.setFoto(R.drawable.banana);
        produto1.setPreco(5);
        produto1.setQuantidade(20);
        produto1.setDescricao("Banana Doce");

        produtos.add(produto1);

        Produto produto2 = new Produto();
        produto2.setNome("Doce");
        produto2.setDescricao("Doce de leite");
        produto2.setFoto(R.drawable.doce);
        produto2.setPreco(3);
        produto2.setQuantidade(15);
        produto2.setDescricao("Doce caseiro");

        produtos.add(produto2);
    }*/

    public void listarProdutos() {
        String email = auth.getCurrentUser().getEmail();
        String idUser = Base64Custom.codificarBase(email);

        produtoRef = firebaseRef.child("produto").child(idUser);

        valueEventListenerProduto = produtoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dados: snapshot.getChildren()){
                    Produto produto = dados.getValue(Produto.class);
                    produtos.add(produto);
                    //Log.i("msg", produtos.toString());
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
