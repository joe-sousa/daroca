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
    private ImageView comerciante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_usuario);

        setTitle("Categoria de usuários");
        setTitleColor(R.color.white);

        produtor = findViewById(R.id.produtorId);
        comerciante = findViewById(R.id.empresarioId);

    }

    public void escolherProdutor(View view){
        Intent intent = new Intent(getApplicationContext(), CadastrarActivity.class);
        intent.putExtra("produtor", "produtor");
        startActivity(intent);
    }

    public void escolherEmpresario(View view){
        Intent intent = new Intent(getApplicationContext(), CadastrarActivity.class);
        intent.putExtra("comerciante", "comerciante");
        startActivity(intent);
    }
}