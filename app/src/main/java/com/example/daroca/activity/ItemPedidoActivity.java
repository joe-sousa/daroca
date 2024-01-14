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
    private TextView textViewValorTotal;
    private DatabaseReference firebaseRef;
    private DatabaseReference usuarioRef;
    private DatabaseReference produtorRef;
    private ItemPedido itemPedido;
    private int quantidade = 0;
    private int quantidadeProduto = 0;
    private int qtd = 0;
    private boolean flag = true;

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
            quantidadeProduto = dados.getInt("quantidade", 0);
            produto = new Produto(produtoEmCadastro);
            Log.i("msg", "qtdProdutoNoItemPedido " + quantidadeProduto);
        }

        String idUsuario = produto.getIdUsuario();

        inicializandoItemPedido(idUsuario);
        quantidade=quantidadeProduto;
        calcularPrecoxQtd();

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
            if(quantidade > 0) {
                itemPedido.salvarItemPedido();
                abrirPedidoActivity();
            }else{
                Toast.makeText(ItemPedidoActivity.this,
                        "A quantidade não pode ser menor ou igual a zero",
                        Toast.LENGTH_SHORT).show();
            }

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
        itemPedido.setQuantidade(quantidadeProduto);
        qtdProdutoTextView.setText(String.valueOf(quantidadeProduto));
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
        textViewValorTotal = findViewById(R.id.valorTotalDoPedido);
    }

    private void abrirPedidoActivity() {
        Log.i("abrirPedidoActivity", "Iniciando PedidoActivity");
        if (itemPedido != null && itemPedido.getProduto() != null && itemPedido.getIdProdutor() != null) {
            Intent myIntent = new Intent(getApplicationContext(), PedidoActivity.class);
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
        }else{
            Log.e("abrirPedidoActivity", "Dados insuficientes para iniciar PedidoActivity");
            // Adicione uma mensagem de log ou feedback para identificar o problema
            Toast.makeText(ItemPedidoActivity.this, "Dados insuficientes para iniciar PedidoActivity", Toast.LENGTH_SHORT).show();
        }
//        startActivity(myIntent);
    }

    private void decrementarQuantidade() {
        flag = false;
        if(quantidade == 0){
            Toast.makeText(ItemPedidoActivity.this,
                    "Valor mínimo para quantidade é " + quantidadeProduto,
                    Toast.LENGTH_SHORT).show();
        }else{
            quantidade-=quantidadeProduto;
            itemPedido.setQuantidade(quantidade);
            qtdProdutoTextView.setText(String.valueOf(itemPedido.getQuantidade()));
            calcularPrecoxQtd();
        }
    }
    private void incrementarQuantidade() {
        flag = true;
        quantidade+=quantidadeProduto;
        if(quantidade < 1){
            Toast.makeText(ItemPedidoActivity.this,
                    "Valor mínimo para quantidade é 1",
                    Toast.LENGTH_SHORT).show();
        }else{
            itemPedido.setQuantidade(quantidade);
        }
        calcularPrecoxQtd();
            Log.i("msg", "Qtd " + itemPedido.getQuantidade());
        qtdProdutoTextView.setText(String.valueOf(itemPedido.getQuantidade()));
    }

    private void calcularValorTotal() {
        double total = itemPedido.getValorPrecoXQtdItem();
        Log.i("msg", "valorFinal " + total);
        itemPedido.setValorTotal(total);
        textViewValorTotal.setText(quantidade + " unidades por R$ " + String.valueOf(total));
    }

    private void calcularPrecoxQtd() {
        if(flag == true){
            qtd++;
        }else{
            qtd--;
        }
        double precoxqtd = qtd * itemPedido.getProduto().getPreco();
        itemPedido.setValorPrecoXQtdItem(precoxqtd);
        calcularValorTotal();
    }
}