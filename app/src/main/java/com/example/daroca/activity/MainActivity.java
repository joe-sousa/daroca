package com.example.daroca.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.daroca.R;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.model.ProductCategory;
import com.example.daroca.model.ProductCategoryItem;
import com.example.daroca.model.Produto;
import com.example.daroca.model.Produtor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.loadProducers();
    }

    private void loadProducers(){
        firebaseRef.child("usuario/produtor").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    HashMap<String, Produtor> producers = new HashMap<>();

                    for (DataSnapshot data: task.getResult().getChildren()) {
                        Produtor produtor = data.getValue(Produtor.class);
                        Log.d("Producer ======== ", produtor.getNome());
                        producers.put(produtor.getIdUsuario(), produtor);
                    }

                    test(producers);
                }
            }
        });

    }

    private void loadProducts(){
        firebaseRef.child("produto").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    for (DataSnapshot data: task.getResult().getChildren()) {
                        Log.d("Produtct Data =====", String.valueOf(data.getValue()));
                    }
                }
            }
        });
    }

    public void test(Map<String, Produtor> producers){
        ArrayList<Task> tasks = new ArrayList<>();

        producers.forEach((key, producer) -> {
            tasks.add(firebaseRef.child("produto" + "/" + producer.getIdUsuario()).get());
        });


        HashMap<String, List<ProducerMainProduct>> productsPerCategory = new HashMap<>();

        String[] productCategories = getResources().getStringArray(R.array.lista_categorias_produtos);

        for (int i = 0; i < productCategories.length; i++) {
            productsPerCategory.put(productCategories[i], new ArrayList<ProducerMainProduct>());
        }

        Task<List<Object>> combinedTask = Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> producersProductList) {
                producersProductList.forEach(producerProductList -> {
                    DataSnapshot parsed = (DataSnapshot) producerProductList;

                    DataSnapshot data = parsed.getChildren().iterator().next();
                    Produto produto = data.getValue(Produto.class);

                    assert produto != null;;

                    List<ProducerMainProduct> categoryProducts = productsPerCategory.get(produto.getCategoria());

                    ProducerMainProduct producerMainProduct = new ProducerMainProduct();
                    producerMainProduct.producer = producers.get(produto.getIdUsuario());
                    producerMainProduct.product = produto;

                    assert categoryProducts != null;

                    categoryProducts.add(producerMainProduct);
                    productsPerCategory.put(produto.getCategoria(), categoryProducts);
                });

                List<ProductCategory> productCategories = new ArrayList<>();

                productsPerCategory.forEach((categoryName, categoryProducts) -> {
                    List<ProductCategoryItem> categoryItems = new ArrayList<>();

                    categoryProducts.forEach(categoryProduct -> {
                        ProductCategoryItem productCategoryItem = new ProductCategoryItem();

                        productCategoryItem.setId(categoryProduct.product.getKey());
                        productCategoryItem.setName(categoryProduct.product.getNome());
                    });

                    ProductCategory productCategory = new ProductCategory(categoryName, categoryItems);
                    productCategories.add(productCategory);
                });
            }
        });
    }

    static class ProducerMainProduct{
        Produtor producer;
        Produto product;

        @Override
        public String toString() {
            return "ProducerMainProduct{" +
                    "producer=" + producer +
                    ", product=" + product +
                    '}';
        }
    }
}
