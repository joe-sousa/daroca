package com.example.daroca.model;

import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.google.firebase.database.DatabaseReference;

public class Comerciante extends Usuario{

    public Comerciante() {
    }

    private String endereco;

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    private String tipo = "comerciante";

    public void salvarComerciante(){
        DatabaseReference firebase = ConfiguracaoAuthFirebase.getFirebaseDatabase();
        firebase.child("usuario")
                .child("comerciante")
                .child(this.getIdUsuario())
                .setValue(this);
    }
}
