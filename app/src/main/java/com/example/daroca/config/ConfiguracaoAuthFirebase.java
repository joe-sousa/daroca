package com.example.daroca.config;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoAuthFirebase {
    private static FirebaseAuth autenticacao;

    private static DatabaseReference firebase;

    private static StorageReference storage;

    public static FirebaseAuth getFirebaseAutenticacao() {
            if (autenticacao == null) {
                autenticacao = FirebaseAuth.getInstance();
            }
            return autenticacao;
    }

    //Retorna a instância do firebase
    public static DatabaseReference getFirebaseDatabase(){
        if(firebase == null){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

    public static StorageReference getFirebaseStorage(){
        if(storage == null){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }

}
