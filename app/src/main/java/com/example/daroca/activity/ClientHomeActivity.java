package com.example.daroca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.daroca.R;
import com.example.daroca.adapter.ProductCategoryAdapter;
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
    Map<String, List<MainActivity.ProducerMainProduct>> productCategoriesMap = genereteProductCategoriesHashMap();

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

  private Map<String, List<MainActivity.ProducerMainProduct>> genereteProductCategoriesHashMap(){
    Map<String, List<MainActivity.ProducerMainProduct>> productCategoriesMap = new HashMap<>();

    String[] productCategories = getResources().getStringArray(R.array.lista_categorias_produtos);

    for (String productCategory : productCategories) {
      productCategoriesMap.put(productCategory, new ArrayList<MainActivity.ProducerMainProduct>());
    }

    return productCategoriesMap;
  }

  private void groupProductsPerCategory(
          List<Object> producersProductList,
          Map<String, List<MainActivity.ProducerMainProduct>> productCategoriesMap,
          Map<String, Produtor> producers
  ){
    producersProductList.forEach(producerProductList -> {
      DataSnapshot producerProductListDataSnapshot = (DataSnapshot) producerProductList;

      DataSnapshot producerFirstProductDataSnapshot = producerProductListDataSnapshot.getChildren().iterator().next();
      Produto producerFirstProduct = producerFirstProductDataSnapshot.getValue(Produto.class);

      assert producerFirstProduct != null;;

      List<MainActivity.ProducerMainProduct> categoryProducts = productCategoriesMap.get(producerFirstProduct.getCategoria());

      MainActivity.ProducerMainProduct producerMainProduct = new MainActivity.ProducerMainProduct();
      producerMainProduct.producer = producers.get(producerFirstProduct.getIdUsuario());
      producerMainProduct.product = producerFirstProduct;

      assert categoryProducts != null;

      categoryProducts.add(producerMainProduct);

      productCategoriesMap.put(producerFirstProduct.getCategoria(), categoryProducts);
    });

  }

  private List<ProductCategory> generateProductCategoriesToRecyclerView(
          Map<String, List<MainActivity.ProducerMainProduct>> productCategoriesMap
  ){
    List<ProductCategory> productCategories = new ArrayList<>();

    productCategoriesMap.forEach((categoryName, categoryProducts) -> {
      List<ProductCategoryItem> categoryItems = new ArrayList<>();

      categoryProducts.forEach(categoryProduct -> {
        ProductCategoryItem productCategoryItem = new ProductCategoryItem();

        productCategoryItem.setId(categoryProduct.product.getKey());
        productCategoryItem.setName(categoryProduct.product.getNome());
        productCategoryItem.setStockTotal(categoryProduct.product.getQuantidade());
        productCategoryItem.setPrice(categoryProduct.product.getPreco());
        productCategoryItem.setImage(categoryProduct.product.getFoto());

        categoryItems.add(productCategoryItem);
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
            item -> Toast.makeText(this, item.getName(), Toast.LENGTH_LONG).show()
    );

    productCategoriesRecyclerView.setLayoutManager(layoutManager);
    productCategoriesRecyclerView.setAdapter(productCategoryAdapter);
  }

   class ProducerMainProduct{
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