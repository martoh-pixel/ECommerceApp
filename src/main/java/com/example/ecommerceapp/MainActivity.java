package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapp.viewmodels.CartViewModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView productsRecyclerView;
    private CartViewModel cartViewModel;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        setupRecyclerView();
        loadProducts();
    }

    private void setupRecyclerView() {
        if (productsRecyclerView != null) {
            productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            productsRecyclerView.setHasFixedSize(true);
        }
    }

    private void loadProducts() {
        List<product> productList = getSampleProducts();

        ProductAdapter productAdapter = new ProductAdapter(productList, productItem -> addToCart(productItem));

        if (productsRecyclerView != null) {
            productsRecyclerView.setAdapter(productAdapter);
        }
    }

    private void addToCart(product productItem) {
        if (cartViewModel != null) {
            cartViewModel.addToCart(productItem, 1);
            Toast.makeText(this, "Added " + productItem.getName() + " to cart", Toast.LENGTH_SHORT).show();
            
            // Proceed to cart automatically after adding an item
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }
    }

    private List<product> getSampleProducts() {
        List<product> products = new ArrayList<>();

        products.add(new product(
                "1",
                "Smartphone X",
                699.99,
                "Latest smartphone with amazing features",
                "android.resource://com.example.ecommerceapp/drawable/product1",
                "Electronics",
                50
        ));

        products.add(new product(
                "2",
                "Laptop Pro",
                1299.99,
                "Powerful laptop for professionals",
                "android.resource://com.example.ecommerceapp/drawable/product1",
                "Electronics",
                30
        ));

        products.add(new product(
                "3",
                "Wireless Headphones",
                199.99,
                "Noise cancelling headphones",
                "android.resource://com.example.ecommerceapp/drawable/product1",
                "Audio",
                100
        ));

        return products;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
