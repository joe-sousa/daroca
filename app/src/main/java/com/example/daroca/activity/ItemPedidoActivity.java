package com.example.daroca.activity;

import android.content.Intent;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.daroca.R;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.UsuarioFirebase;
import com.example.daroca.model.ItemPedido;
import com.example.daroca.model.Pedido;
import com.example.daroca.model.Produto;
import com.example.daroca.model.Produtor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ItemPedidoActivity extends AppCompatActivity {

    Produto produto;
    private TextView itemProdutoTextView;
    private TextView nomeProdutoTextView;
    private TextView descricaoProdutoTextView;
    private TextView precoProdutoTextView;
    private TextView qtdProdutoTextView;
    private ImageButton aumentarButton;
    private ImageButton diminuirButton;
    private Button avancarTelaPedidos;
    private TextView textView;
    private DatabaseReference firebaseRef;
    private DatabaseReference usuarioRef;
    private DatabaseReference produtorRef;
    private ItemPedido itemPedido;
    private String idProdutor;
    private int quantidade = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_pedido);

        firebaseRef = FirebaseDatabase.getInstance().getReference();

        inicializandoAtributosDoItemPedido();

        produto = new Produto();

        Bundle dados = getIntent().getExtras();
        if (dados != null) {
            //Recebendo dados do produto da PrincipalClienteActivity através do ProdutorAdapterCliente.java
            Produto produtoEmCadastro = (Produto) dados.getSerializable("objeto1");
            produto = new Produto(produtoEmCadastro);
        }

        String idUsuario = produto.getIdUsuario();

        inicializandoItemPedido(idUsuario);

        aumentarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementarQuantidade();
            }
        });

        diminuirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementarQuantidade();
            }
        });

    avancarTelaPedidos.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calcularPrecoxQtd();
            abrirPedidoActivity();
            itemPedido.salvarItemPedido();
        }
    });
    }

    private void inicializandoItemPedido(String idUsuario) {
        nomeProdutoTextView.setText(produto.getNome());
        descricaoProdutoTextView.setText(produto.getDescricao());
        precoProdutoTextView.setText(String.valueOf(produto.getPreco()));
        itemPedido = new ItemPedido();
        itemPedido.setProduto(produto);
        itemPedido.setIdProdutor(idUsuario);
        qtdProdutoTextView.setText(String.valueOf(itemPedido.getQuantidade()));
    }

    private void inicializandoAtributosDoItemPedido() {
        itemProdutoTextView = findViewById(R.id.textTesteItemPedido);
        nomeProdutoTextView = findViewById(R.id.textNome);
        descricaoProdutoTextView = findViewById(R.id.textoDescricaoProd);
        precoProdutoTextView = findViewById(R.id.textoPreco);
        diminuirButton = findViewById(R.id.imageButtonDiminuirQtd);
        aumentarButton = findViewById(R.id.imageButtonAumentarQtd);
        qtdProdutoTextView = findViewById(R.id.idQuantidadeItensPedido);
        avancarTelaPedidos = findViewById(R.id.buttonIncluirPedido);
        textView = findViewById(R.id.textViewIdProdutorDaSacola);
    }

    private void abrirPedidoActivity() {
        Log.i("abrirPedidoActivity", "Iniciando PedidoActivity");
        Intent myIntent = new Intent(ItemPedidoActivity.this, PedidoActivity.class);
        myIntent.putExtra("objeto2", itemPedido.getProduto());
        myIntent.putExtra("idProdutor", itemPedido.getIdProdutor());
        myIntent.putExtra("quantidade", itemPedido.getQuantidade());
        try {
            startActivity(myIntent);
            Log.i("abrirPedidoActivity", "PedidoActivity iniciada com sucesso");
        } catch (Exception e) {
            Log.e("abrirPedidoActivity", "Erro ao iniciar PedidoActivity: " + e.getMessage());
            e.printStackTrace();
        }
        //startActivity(myIntent);
    }

    private void decrementarQuantidade() {
        if(quantidade < 1){
            Toast.makeText(ItemPedidoActivity.this,
                    "Valor mínimo para quantidade é 1",
                    Toast.LENGTH_SHORT).show();
        }else{
            quantidade--;
        }
        itemPedido.setQuantidade(quantidade);
        qtdProdutoTextView.setText(String.valueOf(itemPedido.getQuantidade()));
        calcularPrecoxQtd();
    }

    private void incrementarQuantidade() {
        quantidade++;
        if(quantidade < 1){
            Toast.makeText(ItemPedidoActivity.this,
                    "Valor mínimo para quantidade é 1",
                    Toast.LENGTH_SHORT).show();
        }else{
            itemPedido.setQuantidade(quantidade);
        }
        qtdProdutoTextView.setText(String.valueOf(itemPedido.getQuantidade()));
        calcularPrecoxQtd();
    }

    private void calcularValorTotal() {
        double total = itemPedido.getValorPrecoXQtdItem();
        Log.i("msg", "valorFinal " + total);
        itemPedido.setValorTotal(total);
    }

    private void calcularPrecoxQtd() {
        double precoxqtd = itemPedido.getQuantidade() * itemPedido.getProduto().getPreco();
        itemPedido.setValorPrecoXQtdItem(precoxqtd);
        calcularValorTotal();
    }
}