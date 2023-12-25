package com.example.daroca.model;

import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Produtor extends Usuario implements Serializable {
    private String chavePix;
    public Produtor() {
    }

    private String foto;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public void salvarProdutor(){
        DatabaseReference firebase = ConfiguracaoAuthFirebase.getFirebaseDatabase();
        firebase.child("usuario")
                .child("produtor")
                .child(this.getIdUsuario())
                .setValue(this);
    }

}
