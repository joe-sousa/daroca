package com.example.daroca.model;

import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Pedido {
    private DatabaseReference pedidoRef;
    private List<ItemPedido> itensPedido;
    private String idPedido;

    private List<ItemPedido> listaPedidos;
    private String formaEntrega;
    double valorTotal;
    public Pedido() {
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemPedido> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(List<ItemPedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    public String getFormaEntrega() {
        return formaEntrega;
    }

    public void setFormaEntrega(String formaEntrega) {
        this.formaEntrega = formaEntrega;
    }

    public void salvarValorTotal() {
        pedidoRef = ConfiguracaoAuthFirebase.getFirebaseDatabase()
                .child("pedido")
                .child(UsuarioFirebase.getIdentificadorUsuario());

        pedidoRef.child("valorTotal")
                .setValue(valorTotal);
    }
}
