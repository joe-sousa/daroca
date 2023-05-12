package com.example.daroca.activity;

import static com.example.daroca.activity.PrincipalClienteActivity.posicaoProduto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class ItemPedidoActivity extends AppCompatActivity {

    Produto produto;
    private TextView item_produto;
    private TextView nomeProduto;
    private TextView descricaoProduto;
    private TextView precoProduto;
    private TextView qtdProduto;
    private ImageButton aumentar;
    private ImageButton diminuir;
    private Button avancarTelaPedidos;
    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();
    ValueEventListener valueEventListenerProdutorName;
    private DatabaseReference usuarioRef;
    private DatabaseReference produtorRef;

    String nomeProdutor;
    int quantidade = 0;
    ItemPedido itemPedido;
    static double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_pedido);

        item_produto = findViewById(R.id.textTesteItemPedido);
        nomeProduto = findViewById(R.id.textNome);
        descricaoProduto = findViewById(R.id.textoDescricaoProd);
        precoProduto = findViewById(R.id.textoPreco);
        diminuir = findViewById(R.id.imageButtonDiminuirQtd);
        aumentar = findViewById(R.id.imageButtonAumentarQtd);
        qtdProduto = findViewById(R.id.idQuantidadeItensPedido);
        avancarTelaPedidos = findViewById(R.id.buttonIncluirPedido);

        produto = new Produto();

        Bundle dados = getIntent().getExtras();
        //Recebendo dados do produto da PrincipalClienteActivity
        Produto produtoEmCadastro = (Produto) dados.getSerializable("objeto");
        produto.setNome(produtoEmCadastro.getNome());
        produto.setDescricao(produtoEmCadastro.getDescricao());
        produto.setPreco(produtoEmCadastro.getPreco());
        produto.setIdUsuario(produtoEmCadastro.getIdUsuario());

        String idUsuario = produto.getIdUsuario();
        usuarioRef = firebaseRef.child("usuario")
                .child("produtor")
                .child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Produtor produtor = snapshot.getValue(Produtor.class);
                nomeProdutor = produtor.getNome();
                nomeProduto.setText(produto.getNome());
                descricaoProduto.setText(produto.getDescricao());
                precoProduto.setText(String.valueOf(produto.getPreco()));
                itemPedido = new ItemPedido();
                itemPedido.setProduto(produto);
                itemPedido.setNomeProdutor(nomeProdutor);
                qtdProduto.setText(String.valueOf(itemPedido.getQuantidade()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        aumentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++quantidade;
                if(quantidade < 1){
                    Toast.makeText(ItemPedidoActivity.this,
                            "Valor mínimo para quantidade é 1",
                            Toast.LENGTH_SHORT).show();
                }else{
                    itemPedido.setQuantidade(quantidade);
                }
                qtdProduto.setText(String.valueOf(itemPedido.getQuantidade()));
            }
        });

        diminuir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantidade < 1){
                    Toast.makeText(ItemPedidoActivity.this,
                            "Valor mínimo para quantidade é 1",
                            Toast.LENGTH_SHORT).show();
                }else{
                    --quantidade;
                }
                itemPedido.setQuantidade(quantidade);
                qtdProduto.setText(String.valueOf(itemPedido.getQuantidade()));
            }
        });

    avancarTelaPedidos.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calcularPrecoxQtd();
            Intent intent = new Intent(getApplicationContext(), PedidoActivity.class);
            intent.putExtra("objeto", itemPedido.getProduto());
            intent.putExtra("nomeProdutor", itemPedido.getNomeProdutor());
            intent.putExtra("quantidade", itemPedido.getQuantidade());
            startActivity(intent);
        }
    });
    }

    private void calcularValorTotal() {
        total+=itemPedido.getValorPrecoXQtdItem();
        Log.i("msg", "valorFinal " + total);
        itemPedido.setValorTotal(total);
    }

    private void calcularPrecoxQtd() {
        float precoxqtd = (float) (itemPedido.getQuantidade() * itemPedido.getProduto().getPreco());
        itemPedido.setValorPrecoXQtdItem(precoxqtd);
        calcularValorTotal();
        itemPedido.salvarItemPedido();
    }
}