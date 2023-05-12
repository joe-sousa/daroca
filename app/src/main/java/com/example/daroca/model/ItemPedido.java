package com.example.daroca.model;

import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;

public class ItemPedido{
    private int quantidade;
    private Produto produto;
    double valorPrecoXQtdItem;
    double valorTotal;
    private String nomeProdutor;
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
    public String getNomeProdutor() {
        return nomeProdutor;
    }
    public void setNomeProdutor(String nomeProdutor) {
        this.nomeProdutor = nomeProdutor;
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
