package com.example.daroca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daroca.R;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.Base64Custom;
import com.example.daroca.model.Cliente;
import com.example.daroca.model.Comerciante;
import com.example.daroca.model.Produtor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

public class CadastrarActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha, campoPix;
    private Button cadastrarUsuario;

    FirebaseAuth autenticacao;

    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();

    private Produtor produtor;
    private Comerciante comerciante;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editPassword);
        campoPix = findViewById(R.id.editPix);
        cadastrarUsuario = findViewById(R.id.buttonCadastro);

        Bundle dados = getIntent().getExtras();

        String categoriaProdutor = dados.getString("produtor");
        String categoriaComerciante = dados.getString("comerciante");

        cadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = campoNome.getText().toString();
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();
                String pix = campoPix.getText().toString();

                if(nome.length() == 0){
                    Toast.makeText(CadastrarActivity.this,
                            "Preencha o nome",
                            Toast.LENGTH_LONG).show();
                }else{
                    if(email.length() == 0){
                        Toast.makeText(CadastrarActivity.this,
                                "Preencha o email",
                                Toast.LENGTH_LONG).show();
                    }else{
                        if(senha.length() == 0) {
                            Toast.makeText(CadastrarActivity.this,
                                    "Preencha a senha",
                                    Toast.LENGTH_LONG).show();
                        }else{
                           if(categoriaProdutor != null){
                                    criarProdutor(nome, email, senha, pix);
                                }else if(categoriaComerciante != null){
//                                    Toast.makeText(CadastrarActivity.this,
//                                            "Recursos para empresário serão liberados em breve",
//                                            Toast.LENGTH_LONG).show();
                                    criarComerciante(nome, email, senha);
                                }
                            }
                        }
                    }
                }
        });
    }

    private void criarComerciante(String nome, String email, String senha) {
        comerciante = new Comerciante();
        comerciante.setNome(nome);
        comerciante.setEmail(email);
        comerciante.setSenha(senha);
        comerciante.setTipo("comerciante");
        cadastrarComerciante();
    }

    private void criarProdutor(String nome, String email, String senha, String pix) {
        produtor = new Produtor();
        produtor.setNome(nome);
        produtor.setEmail(email);
        produtor.setSenha(senha);
        produtor.setChavePix(pix);
        produtor.setTipo("produtor");
        cadastrarProdutor();
    }

    //Métodos para cadastrar o usuarios usando DesignPattern Singleton com a classe ConfiguracaoAuthFirebase

    public void cadastrarProdutor(){
        autenticacao = ConfiguracaoAuthFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(produtor.getEmail(),
                produtor.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String idProdutor = Base64Custom.codificarBase(produtor.getEmail());
                    produtor.setIdUsuario(idProdutor);
                    produtor.salvarProdutor();
                    Toast.makeText(CadastrarActivity.this,
                            "Cadastro concluído com sucesso.",
                            Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                }else{
                    validarMetodos(task);
                }
            }
        });
    }

    public void cadastrarComerciante(){
        autenticacao = ConfiguracaoAuthFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(comerciante.getEmail(),
                comerciante.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String idComerciante = Base64Custom.codificarBase(comerciante.getEmail());
                comerciante.setIdUsuario(idComerciante);
                comerciante.salvarComerciante();
                if(task.isSuccessful()){
                    Toast.makeText(CadastrarActivity.this,
                            "Cadastro concluído com sucesso.",
                            Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                }else{
                    validarMetodos(task);
                }
            }
        });
    }

    private void validarMetodos(@NonNull Task<AuthResult> task) {
        String excecao = "";
        try {
            throw task.getException();
        }catch (FirebaseAuthWeakPasswordException e){
            excecao = "Digite uma senha mais forte.";
        }catch (FirebaseAuthInvalidCredentialsException e){
            excecao = "Por favor, digite um email válido.";
        }catch(FirebaseAuthUserCollisionException e){
            excecao = "Esse email já existe, digite outro.";
        }catch (Exception e){
            excecao = "Erro ao cadastrar usuário: " + e.getMessage();
            e.printStackTrace();
        }
        Toast.makeText(CadastrarActivity.this,
                excecao,
                Toast.LENGTH_LONG).show();
    }
}
