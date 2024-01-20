package com.example.daroca.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ProdutoresActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProdutor;
    private List<Produtor> produtores = new ArrayList<>();
    ProdutorAdapter produtorAdapter;

    private DatabaseReference produtorRef;
    private DatabaseReference produtoRef;
    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();

    public static int posicaoProdutor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtores);
        setTitle("Produtores");
        recyclerViewProdutor = findViewById(R.id.recyclerViewProdutor);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewProdutor.setLayoutManager(layoutManager);
        produtorAdapter = new ProdutorAdapter(produtores);
        recyclerViewProdutor.setAdapter(produtorAdapter);
        recyclerViewProdutor.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerViewProdutor,
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