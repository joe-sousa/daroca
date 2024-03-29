package com.example.daroca.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permission {
    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requesCode){
        if(Build.VERSION.SDK_INT >= 23){
            List<String> listaPermissoes = new ArrayList<>();
            for(String permissao : permissoes){
                boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if(!temPermissao) listaPermissoes.add(permissao);
            }
            if(listaPermissoes.isEmpty()) return true;

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            ActivityCompat.requestPermissions(activity, novasPermissoes, requesCode);
        }
        return true;
    }
}
