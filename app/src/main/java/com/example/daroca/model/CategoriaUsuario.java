package com.example.daroca.model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.daroca.R;
import com.example.daroca.activity.CadastrarActivity;

public class CategoriaUsuario extends AppCompatActivity {

    private ImageView produtor;
    private ImageView cliente;
    private ImageView comerciante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_usuario);

        setTitle("Categoria de usuários");
        setTitleColor(R.color.white);

        produtor = findViewById(R.id.produtorId);
        cliente = findViewById(R.id.clienteId);
        comerciante = findViewById(R.id.empresarioId);

    }

    public void escolherProdutor(View view){
        Intent intent = new Intent(getApplicationContext(), CadastrarActivity.class);
        intent.putExtra("categoria1", "produtor");
        startActivity(intent);
    }

    public void escolherCliente(View view){
        Intent intent = new Intent(getApplicationContext(), CadastrarActivity.class);
        intent.putExtra("categoria2", "cliente");
        startActivity(intent);
    }
    public void escolherEmpresario(View view){
        Intent intent = new Intent(getApplicationContext(), CadastrarActivity.class);
        intent.putExtra("categoria3", "empresario");
        startActivity(intent);
    }
}