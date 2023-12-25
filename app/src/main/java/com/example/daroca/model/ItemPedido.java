package com.example.daroca.model;

import static com.example.daroca.activity.PrincipalClienteActivity.posicaoProduto;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.daroca.R;
import com.example.daroca.activity.ItemPedidoActivity;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ItemPedido{
    private int quantidade;
    private Produto produto;
    double valorPrecoXQtdItem;
    double valorTotal;
    private String idProdutor;

    public ItemPedido() {
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getIdProdutor() {
        return idProdutor;
    }

    public void setIdProdutor(String idProdutor) {
        this.idProdutor = idProdutor;
    }

    public double getValorPrecoXQtdItem() {
        return valorPrecoXQtdItem;
    }

    public void setValorPrecoXQtdItem(double valorPrecoXQtdItem) {
        this.valorPrecoXQtdItem = valorPrecoXQtdItem;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void salvarItemPedido(){
        DatabaseReference firebase = ConfiguracaoAuthFirebase.getFirebaseDatabase();
        firebase.child("pedido")
                .child(UsuarioFirebase.getIdentificadorUsuario())
                .child("itemPedido")
                .push()
                .setValue(this);
    }
}
