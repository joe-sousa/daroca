package com.example.daroca.activity;

import static com.example.daroca.activity.PrincipalClienteActivity.posicaoProduto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.daroca.R;
import com.example.daroca.adapter.ProductCategoryAdapter;
import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.example.daroca.model.ProductCategory;
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

public class ClientHomeActivity extends AppCompatActivity {
  private final DatabaseReference firebaseRef = ConfiguracaoAuthFirebase.getFirebaseDatabase();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_client_home);

    this.loadProducers();
  }

  private void loadProducers(){
    firebaseRef.child("usuario/produtor").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DataSnapshot> task) {
        if (!task.isSuccessful()) {
          Log.e("FIREBASE ERROR ===== ", "Loading producers error", task.getException());

        } else {
          HashMap<String, Produtor> producers = new HashMap<>();

          for (DataSnapshot data: task.getResult().getChildren()) {
            Produtor produtor = data.getValue(Produtor.class);

            assert produtor != null;

            producers.put(produtor.getIdUsuario(), produtor);
          }

          loadProductCategories(producers);
        }
      }
    });

  }

  private void loadProductCategories(Map<String, Produtor> producers){
    List<Task<DataSnapshot>> loadProducersProductListTasks = generateLoadAllProducersProductListTasks(producers);
    Map<String, List<ProducerMainProduct>> productCategoriesMap = genereteProductCategoriesHashMap();

    Tasks.whenAllSuccess(loadProducersProductListTasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
      @Override
      public void onSuccess(List<Object> producersProductList) {
        groupProductsPerCategory(producersProductList, productCategoriesMap, producers);
        List<ProductCategory> productCategories = generateProductCategoriesToRecyclerView(productCategoriesMap);

        initProductCategoriesRecyclerView(productCategories);
      }
    });
  }

  private List<Task<DataSnapshot>> generateLoadAllProducersProductListTasks(Map<String, Produtor> producers){
    List<Task<DataSnapshot>> tasks = new ArrayList<>();

    producers.forEach((key, producer) -> {
      tasks.add(firebaseRef.child("produto/" + producer.getIdUsuario()).get());
    });

    return tasks;
  }

  private Map<String, List<ProducerMainProduct>> genereteProductCategoriesHashMap(){
    Map<String, List<ProducerMainProduct>> productCategoriesMap = new HashMap<>();

    String[] productCategories = getResources().getStringArray(R.array.lista_categorias_produtos);

    for (String productCategory : productCategories) {
      productCategoriesMap.put(productCategory, new ArrayList<ProducerMainProduct>());
    }

    return productCategoriesMap;
  }

  private void groupProductsPerCategory(
          List<Object> producersProductList,
          Map<String, List<ProducerMainProduct>> productCategoriesMap,
          Map<String, Produtor> producers
  ){
    producersProductList.forEach(producerProductList -> {
      DataSnapshot producerProductListDataSnapshot = (DataSnapshot) producerProductList;

      for (DataSnapshot producerProductDataSnapshot : producerProductListDataSnapshot.getChildren()){
        Produto producerProduct = producerProductDataSnapshot.getValue(Produto.class);

        assert producerProduct != null;;
        producerProduct.setKey(producerProductDataSnapshot.getKey());

        List<ProducerMainProduct> categoryProducts = productCategoriesMap.get(producerProduct.getCategoria());

        ProducerMainProduct producerMainProduct = new ProducerMainProduct();
        producerMainProduct.producer = producers.get(producerProduct.getIdUsuario());
        producerMainProduct.product = producerProduct;

        assert categoryProducts != null;

        categoryProducts.add(producerMainProduct);

        productCategoriesMap.put(producerProduct.getCategoria(), categoryProducts);
      }

    });

  }

  private List<ProductCategory> generateProductCategoriesToRecyclerView(
          Map<String, List<ProducerMainProduct>> productCategoriesMap
  ){
    List<ProductCategory> productCategories = new ArrayList<>();

    productCategoriesMap.forEach((categoryName, categoryProducts) -> {
      List<Produto> categoryItems = new ArrayList<>();

      categoryProducts.forEach(categoryProduct -> {
        categoryItems.add(categoryProduct.product);
      });

      if(categoryItems.size() > 0){
        ProductCategory productCategory = new ProductCategory(categoryName, categoryItems);
        productCategories.add(productCategory);
      }
    });

    return productCategories;
  }

  private void initProductCategoriesRecyclerView(List<ProductCategory> productCategories) {
    RecyclerView productCategoriesRecyclerView = findViewById(R.id.productCategoriesRecyclerView);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    ProductCategoryAdapter productCategoryAdapter = new ProductCategoryAdapter(
            productCategories,
            item -> {
              Intent intent = new Intent(this, ItemPedidoActivity.class);
              intent.putExtra("objeto1", item);
              intent.putExtra("quantidade", item.getQuantidade());
              startActivity(intent);
            }
    );

    productCategoriesRecyclerView.setLayoutManager(layoutManager);
    productCategoriesRecyclerView.setAdapter(productCategoryAdapter);
  }

   static class ProducerMainProduct{
    Produtor producer;
    Produto product;

    @NonNull
    @Override
    public String toString() {
      return "ProducerMainProduct{" +
              "producer=" + producer +
              ", product=" + product +
              '}';
    }
  }


}