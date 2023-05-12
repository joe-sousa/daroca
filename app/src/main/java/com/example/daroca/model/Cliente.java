package com.example.daroca.model;

import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.google.firebase.database.DatabaseReference;

public class Cliente extends Usuario{

    public Cliente() {
    }

    private String endereco;

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void salvar(){
        DatabaseReference firebase = ConfiguracaoAuthFirebase.getFirebaseDatabase();
        firebase.child("usuario")
                .child("cliente")
                .child(this.getIdUsuario())
                .setValue(this);
    }
}
