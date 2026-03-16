package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapp.viewmodels.CartViewModel;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private TextView totalTextView;
    private Button checkoutBtn;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Setup toolbar
        if (findViewById(R.id.toolbar) != null) {
            setSupportActionBar(findViewById(R.id.toolbar));
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalTextView = findViewById(R.id.totalTextView);
        checkoutBtn = findViewById(R.id.checkoutBtn);

        // Initialize ViewModel
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Setup RecyclerView
        if (cartRecyclerView != null) {
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            cartAdapter = new CartAdapter(
                    (item, newQuantity) -> cartViewModel.updateItemQuantity(item, newQuantity),
                    item -> cartViewModel.removeItem(item)
            );
            cartRecyclerView.setAdapter(cartAdapter);
        }

        // Observe cart items
        cartViewModel.getCartItems().observe(this, cartItems -> {
            if (cartAdapter != null) {
                cartAdapter.submitList(cartItems);
            }
        });

        // Observe cart total
        cartViewModel.getCartTotal().observe(this, total -> {
            if (totalTextView != null) {
                totalTextView.setText(String.format(Locale.US, "Total: $%.2f", total));
            }
        });

        // Checkout button click
        if (checkoutBtn != null) {
            checkoutBtn.setOnClickListener(v -> {
                cartViewModel.setCheckoutStep(1);
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
