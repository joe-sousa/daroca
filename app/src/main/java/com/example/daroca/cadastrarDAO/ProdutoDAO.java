package com.example.daroca.cadastrarDAO;

import androidx.annotation.NonNull;

import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.Base64Custom;
import com.example.daroca.helper.UsuarioFirebase;
import com.example.daroca.model.Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    public ProdutoDAO() {
    }
}
