package com.example.daroca.model;

import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.google.firebase.database.DatabaseReference;

public class Produtor extends Usuario{
    private String chavePix;

    public Produtor() {
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
