package com.example.daroca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daroca.R;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.Base64Custom;
import com.example.daroca.model.CategoriaUsuario;
import com.example.daroca.model.Cliente;
import com.example.daroca.model.Comerciante;
import com.example.daroca.model.Produtor;
import com.example.daroca.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private TextView buttonCadastrar;
    private EditText senha;
    private Button buttonEntrar;
    private FirebaseAuth autenticacao;

    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();

    private Cliente cliente;
    private Produtor produtor;
    private Comerciante comerciante;
    //private CheckBox checkBox;
    private Usuario user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editEmailLogin);
        senha = findViewById(R.id.editPasswordLogin);
        buttonEntrar = findViewById(R.id.buttonLogar);
        buttonCadastrar = findViewById(R.id.textCadastrar);
        //checkBox = findViewById(R.id.checkBoxSalvarCredenciais);

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eMail = email.getText().toString();
                String password = senha.getText().toString();

                if(eMail.length() == 0){
                    Toast.makeText(LoginActivity.this,
                            "Digite o email",
                            Toast.LENGTH_LONG).show();
                }else{
                    if(password.length() == 0){
                        Toast.makeText(LoginActivity.this,
                                "Digite a senha",
                                Toast.LENGTH_LONG).show();
                    }else{
                            user = new Usuario();
                            user.setEmail(eMail);
                            user.setSenha(password);
                            logarUsuario();
                    }
                }
            }
        });
    }

    private void logarUsuario() {
        autenticacao = ConfiguracaoAuthFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(user.getEmail(), user.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){;
                            direcionarTelaPorCategoria();
                        }else{
                            String excecao = "";
                            try {
                                throw task.getException();
                            }catch(FirebaseAuthInvalidUserException e){
                                excecao = "Por favor, digite um usuário cadastrado.";
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                excecao = "E-mail e senha não correspondem a um usuário cadastrado.";
                            }catch (Exception e){
                                excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this,
                                    excecao,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void direcionarTelaPorCategoria() {
        String email = autenticacao.getCurrentUser().getEmail();
        String idUser = Base64Custom.codificarBase(email);


        DatabaseReference usuarioRef = firebaseRef.child("usuario")
                .child("comerciante");

        DatabaseReference usuarioRef1 = firebaseRef.child("usuario")
                .child("cliente");

        DatabaseReference usuarioRef2 = firebaseRef.child("usuario")
                .child("produtor");

            usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(idUser).exists()){
                            startActivity(new Intent(getApplicationContext(), PrincipalComercianteActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        usuarioRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(idUser).exists()){
                        startActivity(new Intent(getApplicationContext(), ProdutoresActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        usuarioRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(idUser).exists()){
                    startActivity(new Intent(getApplicationContext(), PrincipalProdutorActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void cadastrarUsuario(View view){
        startActivity(new Intent(getApplicationContext(), CategoriaUsuario.class));
    }
}
