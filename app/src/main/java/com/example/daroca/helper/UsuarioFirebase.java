package com.example.daroca.helper;

import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class UsuarioFirebase {

    public static String getIdentificadorUsuario(){
        FirebaseAuth usuario = ConfiguracaoAuthFirebase.getFirebaseAutenticacao();
        String email = usuario.getCurrentUser().getEmail();

        String idUser = Base64Custom.codificarBase(email);

        return idUser;
    }
}
