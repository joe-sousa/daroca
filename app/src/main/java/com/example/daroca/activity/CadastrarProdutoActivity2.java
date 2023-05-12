package com.example.daroca.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.daroca.R;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.helper.Permission;
import com.example.daroca.helper.UsuarioFirebase;
import com.example.daroca.model.Produto;
import com.example.daroca.model.Produtor;
import com.example.daroca.model.TiposRecebimento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class CadastrarProdutoActivity2 extends AppCompatActivity {
    String produtoImageUrl;
    private Spinner spinnerPontosEntrega;
    private ImageView fotoProduto;
    private ImageButton tirarfoto;
    private ImageButton pegarFotoGaleria;
    private EditText taxaEntrega;
    private EditText valorParaEntregaGratis;
    private CheckBox recebimentoPix;
    private CheckBox recebimentoCartao;
    private CheckBox recebimentoDinheiro;
    private CheckBox entregaDelivery;
    private CheckBox entregaPontosEntrega;
    private CheckBox entregaGratis;

    Produto produto;

    //Id do usuário
    private String identificadorUsuario;

    //Referência para recuperar dados do Firebase
    private DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();
    //Referência para recuperar dados de usuários
    private DatabaseReference usuarioRef;
    //referência para manipular dados do usuário
    private ValueEventListener valueEventListenerUser;

    private Button cadastrar;

    //Constantes para selcionar câmera ou galeria
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    //Referência para pasta de imagens
    private StorageReference storageReference;

    //Atributo para receber url da imagem salva no storage

    //Array com informações sobre permissões de uso externo e câmera
    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    Produtor produtor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_produto2);

        setTitle("Cadastro de produtos");

        //Referência para acesso ao Storage
        storageReference = ConfiguracaoAuthFirebase.getFirebaseStorage();

        produto = new Produto();

        Bundle dados = getIntent().getExtras();
        //Recebendo dados do produto da activity 1
        Produto produtoEmCadastro = (Produto) dados.getSerializable("objeto");
        produto.setNome(produtoEmCadastro.getNome());
        produto.setDescricao(produtoEmCadastro.getDescricao());
        produto.setQuantidade(produtoEmCadastro.getQuantidade());
        produto.setPreco(produtoEmCadastro.getPreco());
        produto.setCategoria(produtoEmCadastro.getCategoria());
        produto.setKey(produtoEmCadastro.getKey());

        spinnerPontosEntrega = (Spinner) findViewById(R.id.spinnerLocaisEntrega);

        //Atributos do layout inicializados no método.
        inicializarAtributosLayout();

        spinnerPontosEntrega.setBackgroundColor(R.color.gray);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.lista_enderecos_entrega, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPontosEntrega.setAdapter(adapter1);

        entregaGratis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked() && valorParaEntregaGratis.getText().toString().length() > 0){
                    //if(valorParaEntregaGratis.getText().toString().length() > 0){
                    inicializarTaxaEntrega();
                    //produto.setTaxaEntrega(0);
                        Toast.makeText(CadastrarProdutoActivity2.this,
                                String.valueOf(produto.getValorMinimoParaEntregaGratis()),
                                Toast.LENGTH_SHORT).show();
                }else{
                    buttonView.setChecked(false);
                    Toast.makeText(CadastrarProdutoActivity2.this,
                                "Digite o valor mínimo para entrega grátis",
                                Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Selecionando entrega via delivery com taxa
        entregaDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked() && taxaEntrega.getText().toString().length() > 0){
                    //if(taxaEntrega.getText().toString().length() > 0){
                        double taxaDelivery = Double.parseDouble(taxaEntrega.getText().toString());
                        produto.setTaxaEntrega(taxaDelivery);
                        produto.getTaxaEntrega();
                        Toast.makeText(CadastrarProdutoActivity2.this,
                        String.valueOf(produto.getTaxaEntrega()), Toast.LENGTH_SHORT).show();
                    //}
                }else{
                    buttonView.setChecked(false);
                    Toast.makeText(CadastrarProdutoActivity2.this,
                            "Digite o valor da taxa de entrega",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Selecionando o checkBox pontos de entrega.
        entregaPontosEntrega.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(buttonView.isChecked()){
                        //Selecionando um dos pontos de entrega
                        spinnerPontosEntrega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                produto.setLocaisEntrega(spinnerPontosEntrega.getSelectedItem().toString());
                                Toast.makeText(CadastrarProdutoActivity2.this,
                                        produto.getLocaisEntrega(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
            }
        });

        recebimentoPix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    produto.setTiposRecebimento(TiposRecebimento.PIX.getDescricao());
                    produto.getTiposRecebimento();
                    Toast.makeText(CadastrarProdutoActivity2.this,
                            produto.getTiposRecebimento(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        recebimentoCartao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    produto.setTiposRecebimento(TiposRecebimento.CARTÃO.getDescricao());
                    produto.getTiposRecebimento();
                    Toast.makeText(CadastrarProdutoActivity2.this,
                            produto.getTiposRecebimento(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        recebimentoDinheiro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    produto.setTiposRecebimento(TiposRecebimento.ESPECIE.getDescricao());
                    produto.getTiposRecebimento();
                    Toast.makeText(CadastrarProdutoActivity2.this,
                            produto.getTiposRecebimento(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        tirarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Log.i("teste", "cliquei em tirar foto");
                //Tratar depois o fato de o celular não ter câmera para evitar crash
                startActivityForResult(i, SELECAO_CAMERA);
            }
        });

        pegarFotoGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("teste", "cliquei em acessar galeria");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //Tratar depois o fato de o celular não ter acesso a galeria
                startActivityForResult(intent, SELECAO_GALERIA);
            }
        });
        Permission.validarPermissoes(permissoes, this, 1);
    }

    private double inicializarTaxaEntrega() {
        double compraMinima = Double.parseDouble(valorParaEntregaGratis.getText().toString());
        produto.setValorMinimoParaEntregaGratis(compraMinima);
        return produto.getValorMinimoParaEntregaGratis();
    }

    private void inicializarAtributosLayout() {
        fotoProduto = findViewById(R.id.imageViewFotoProduto);
        tirarfoto = findViewById(R.id.imageCamera);
        pegarFotoGaleria = findViewById(R.id.imageGallery);

        recebimentoPix = findViewById(R.id.checkBoxPix);
        recebimentoCartao = findViewById(R.id.checkBoxCartao);
        recebimentoDinheiro = findViewById(R.id.checkBoxEspecie);

        entregaDelivery = findViewById(R.id.checkBoxDelivery);
        entregaPontosEntrega = findViewById(R.id.checkBoxPontoEntrega);
        entregaGratis = findViewById(R.id.checkBoxGratis);

        taxaEntrega = findViewById(R.id.editTextValorEntrega);
        valorParaEntregaGratis = findViewById(R.id.editTextValorMinimoEntregaGratis);
        cadastrar = findViewById(R.id.buttonCadastrarProduto);
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
    }

    //Metodo para cadastrar novos produtos
    public void cadastrarProduto(View view){

        StorageReference imageRef   = storageReference
                .child("produtos")
                .child("categoria")
                .child(produto.getCategoria())
                .child(identificadorUsuario)
                .child(produto.getNome() + ".jpeg");

        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                produtoImageUrl = task.getResult().toString();
                produto.setIdUsuario(identificadorUsuario);
                produto.setFoto(produtoImageUrl);
                produto.getFoto();
                produto.salvarProduto();
            }
        });
        Toast.makeText(getApplicationContext(),
                            "Cadastro concluído com sucesso.",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), PrincipalProdutorActivity.class));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;
            try {
                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri selectedImageUri = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        break;
                }
                if(imagem != null){
                    fotoProduto.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos =  new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar imagens no firebase
                    StorageReference produtoRef = storageReference
                            .child("produtos")
                            .child("categoria")
                            .child(produto.getCategoria())
                            .child(identificadorUsuario)
                            .child(produto.getNome() + ".jpeg");

                    UploadTask uploadTask = produtoRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CadastrarProdutoActivity2.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CadastrarProdutoActivity2.this,
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacao();
            }
        }
    }

    private void alertaValidacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas.");
        builder.setMessage("Para usar o app é necessário aceitar as permissões.");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}