package com.example.daroca.helper;

import android.util.Base64;

public class Base64Custom {
    public static String codificarBase(String texto){
        return Base64.encodeToString(texto.getBytes(),
                Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decodificarBase(String textoDecodificado){
        return new String(Base64.decode(textoDecodificado, Base64.DEFAULT));
    }
}
