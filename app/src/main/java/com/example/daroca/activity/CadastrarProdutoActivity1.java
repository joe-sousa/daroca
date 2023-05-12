package com.example.daroca.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daroca.R;
import com.example.daroca.model.Produto;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;

public class CadastrarProdutoActivity1 extends AppCompatActivity {
    private EditText nome;
    private EditText descricao;
    private EditText preco;

    private EditText quantidade;

    private String resultadoCategoriaSpinner;
    Spinner spinnerCategoriaProdutos;

    private int resultQtdVendida;
    private Button buttonAvancarTela;

    @SuppressLint({"ResourceType", "MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_produto);

        setTitle("Cadastro de produtos");
        setTitleColor(R.color.white);

        nome = findViewById(R.id.textInputEditTextNomeProduto);
        descricao = findViewById(R.id.textInputEditTextDescricaoProduto);
        preco = findViewById(R.id.textInputEditTextPrecoProduto);
        spinnerCategoriaProdutos = (Spinner) findViewById(R.id.spinnerCategorias);
        quantidade = findViewById(R.id.textInputEditTextQtd);

        buttonAvancarTela = findViewById(R.id.buttonPraTela2CadastroProduto);

        spinnerCategoriaProdutos.setBackgroundColor(R.color.gray);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lista_categorias_produtos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerCategoriaProdutos.setAdapter(adapter);

        spinnerCategoriaProdutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultadoCategoriaSpinner=spinnerCategoriaProdutos.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void avancarTela2CadastroProduto(View view){
        buttonAvancarTela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });
    }

    public void validarCampos(){
        if(nome.getText().toString().length() == 0){
            Toast.makeText(this,
                    "Digite um nome",
                    Toast.LENGTH_SHORT).show();
        } else if (descricao.getText().toString().length() == 0) {
            Toast.makeText(this,
                    "Digite uma descrição",
                    Toast.LENGTH_SHORT).show();
        } else if (preco.getText().toString().length() == 0) {
            Toast.makeText(this,
                    "Digite um preço",
                    Toast.LENGTH_SHORT).show();
        } else if (quantidade.getText().toString().length() == 0) {
            Toast.makeText(this,
                    "Digite uma quantidade",
                    Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(getApplicationContext(), CadastrarProdutoActivity2.class);
            Produto produto = new Produto(descricao.getText().toString(),
                    nome.getText().toString(),
                    Double.parseDouble(preco.getText().toString()),
                    Integer.parseInt(quantidade.getText().toString()),
                    resultadoCategoriaSpinner);
            //Log.i("produto ", produto.getNome() + produto.getDescricao() + produto.getPreco() + produto.getCategoria() + produto.getQuantidade());
            intent.putExtra("objeto", produto);
            startActivity(intent);
        }
    }
}