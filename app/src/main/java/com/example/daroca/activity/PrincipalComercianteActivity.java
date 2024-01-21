package com.example.daroca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.daroca.R;
import com.example.daroca.adapter.ProdutorAdapter;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.RecyclerTouchListener;
import com.example.daroca.model.Produtor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalComercianteActivity extends AppCompatActivity {

    private RecyclerView recyclerViewComerciante;
    private List<Produtor> produtores = new ArrayList<>();
    ProdutorAdapter produtorAdapter;

    private DatabaseReference produtorRef;
    private DatabaseReference produtoRef;
    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();

    public static int posicaoProdutor;

    //TODO SuppressLint é realmente necessario ?
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_comerciante);

        setTitle("Produtores");
        recyclerViewComerciante = findViewById(R.id.recyclerViewComerciante);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewComerciante.setLayoutManager(layoutManager);
        produtorAdapter = new ProdutorAdapter(produtores);
        recyclerViewComerciante.setAdapter(produtorAdapter);
        recyclerViewComerciante.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerViewComerciante,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        posicaoProdutor = position;
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        this.listarProdutores();
    }

    private void listarProdutores() {
        produtorRef = firebaseRef.child("usuario")
                .child("produtor");

        produtoRef = firebaseRef.child("produto");

        produtorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dados: snapshot.getChildren()){
                    Produtor produtor = dados.getValue(Produtor.class);
                    produtores.add(produtor);
                }
                produtorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}