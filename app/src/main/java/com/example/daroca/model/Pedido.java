package com.example.daroca.model;

import android.app.usage.UsageEvents;

import androidx.annotation.NonNull;

import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.events.Event;

import java.util.List;

public class Pedido {

    DatabaseReference pedidoRef;

    DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();

    ValueEventListener pedidoValueEventListener;

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
}
