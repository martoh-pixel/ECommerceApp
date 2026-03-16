package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerceapp.viewmodels.CartViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private CartViewModel cartViewModel;
    private FrameLayout checkoutContainer;
    private TextView step1Text, step2Text, step3Text;
    private ProgressBar progressBar;

    private View step1View, step2View, step3View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        if (findViewById(R.id.toolbar) != null) {
            setSupportActionBar(findViewById(R.id.toolbar));
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkoutContainer = findViewById(R.id.checkoutContainer);
        step1Text = findViewById(R.id.step1Text);
        step2Text = findViewById(R.id.step2Text);
        step3Text = findViewById(R.id.step3Text);
        progressBar = findViewById(R.id.checkoutProgress);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        cartViewModel.getCheckoutProgress().observe(this, step -> {
            updateUIForStep(step);
        });
    }

    private void updateUIForStep(int step) {
        if (progressBar != null) progressBar.setProgress(step * 33);

        int activeColor = getResources().getColor(android.R.color.holo_purple, getTheme());
        int inactiveColor = getResources().getColor(android.R.color.darker_gray, getTheme());

        if (step1Text != null) step1Text.setTextColor(step >= 1 ? activeColor : inactiveColor);
        if (step2Text != null) step2Text.setTextColor(step >= 2 ? activeColor : inactiveColor);
        if (step3Text != null) step3Text.setTextColor(step >= 3 ? activeColor : inactiveColor);

        switch (step) {
            case 1: showStep1(); break;
            case 2: showStep2(); break;
            case 3: showStep3(); break;
        }
    }

    private void showStep1() {
        step1View = getLayoutInflater().inflate(R.layout.checkout_step1_shipping, checkoutContainer, false);
        checkoutContainer.removeAllViews();
        checkoutContainer.addView(step1View);

        Button nextBtn = step1View.findViewById(R.id.nextBtn);
        if (nextBtn != null) {
            nextBtn.setOnClickListener(v -> {
                TextInputEditText nameInput = step1View.findViewById(R.id.fullNameInput);
                if (nameInput != null && nameInput.getText().toString().isEmpty()) {
                    nameInput.setError("Required");
                    return;
                }
                cartViewModel.setCheckoutStep(2);
            });
        }
    }

    private void showStep2() {
        step2View = getLayoutInflater().inflate(R.layout.checkout_step2_payment, checkoutContainer, false);
        checkoutContainer.removeAllViews();
        checkoutContainer.addView(step2View);

        Button nextBtn = step2View.findViewById(R.id.nextBtn);
        Button backBtn = step2View.findViewById(R.id.backBtn);

        if (nextBtn != null) {
            nextBtn.setOnClickListener(v -> {
                TextInputEditText cardInput = step2View.findViewById(R.id.cardNumberInput);
                if (cardInput != null && cardInput.getText().toString().length() < 16) {
                    cardInput.setError("Invalid card number");
                    return;
                }
                cartViewModel.setCheckoutStep(3);
            });
        }
        if (backBtn != null) backBtn.setOnClickListener(v -> cartViewModel.setCheckoutStep(1));
    }

    private void showStep3() {
        step3View = getLayoutInflater().inflate(R.layout.checkout_step3_review, checkoutContainer, false);
        checkoutContainer.removeAllViews();
        checkoutContainer.addView(step3View);

        RecyclerView orderItemsRecyclerView = step3View.findViewById(R.id.orderItemsRecyclerView);
        if (orderItemsRecyclerView != null) {
            orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            CartAdapter orderItemsAdapter = new CartAdapter(null, null);
            orderItemsRecyclerView.setAdapter(orderItemsAdapter);
            cartViewModel.getCartItems().observe(this, items -> {
                orderItemsAdapter.submitList(items);
                updateTotals();
            });
        }

        RecyclerView upsellRecyclerView = step3View.findViewById(R.id.upsellRecyclerView);
        if (upsellRecyclerView != null) {
            upsellRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            List<product> upsellProducts = getUpsellProducts();
            UpsellAdapter upsellAdapter = new UpsellAdapter(upsellProducts, product -> {
                cartViewModel.addToCart(product, 1);
                Toast.makeText(this, "Added " + product.getName() + " to cart", Toast.LENGTH_SHORT).show();
            });
            upsellRecyclerView.setAdapter(upsellAdapter);
        }

        Button placeOrderBtn = step3View.findViewById(R.id.placeOrderBtn);
        Button backBtn = step3View.findViewById(R.id.backBtn);

        if (placeOrderBtn != null) placeOrderBtn.setOnClickListener(v -> placeOrder());
        if (backBtn != null) backBtn.setOnClickListener(v -> cartViewModel.setCheckoutStep(2));
    }

    private void updateTotals() {
        if (step3View == null) return;

        Double total = cartViewModel.getCartTotal().getValue();
        if (total == null) total = 0.0;

        double subtotal = total;
        double tax = subtotal * 0.08;
        double shipping = subtotal > 50 ? 0 : 5.99;
        double grandTotal = subtotal + tax + shipping;

        TextView subtotalTv = step3View.findViewById(R.id.subtotalText);
        TextView taxTv = step3View.findViewById(R.id.taxText);
        TextView shippingTv = step3View.findViewById(R.id.shippingText);
        TextView totalTv = step3View.findViewById(R.id.totalText);

        if (subtotalTv != null) subtotalTv.setText(String.format(Locale.US, "$%.2f", subtotal));
        if (taxTv != null) taxTv.setText(String.format(Locale.US, "$%.2f", tax));
        if (shippingTv != null) shippingTv.setText(String.format(Locale.US, "$%.2f", shipping));
        if (totalTv != null) totalTv.setText(String.format(Locale.US, "$%.2f", grandTotal));
    }

    private List<product> getUpsellProducts() {
        List<product> products = new ArrayList<>();
        products.add(new product("5", "Phone Case", 19.99, "Protective phone case", "https://via.placeholder.com/300?text=Phone+Case", "Accessories", 200));
        products.add(new product("6", "Screen Protector", 9.99, "Tempered glass screen protector", "https://via.placeholder.com/300?text=Screen+Protector", "Accessories", 300));
        return products;
    }

    private void placeOrder() {
        List<cartItem> items = cartViewModel.getCartItems().getValue();
        if (items == null || items.isEmpty()) {
            finish();
            return;
        }

        double subtotal = cartViewModel.getCartTotal().getValue() != null ? cartViewModel.getCartTotal().getValue() : 0.0;
        double tax = subtotal * 0.08;
        double shipping = subtotal > 50 ? 0 : 5.99;
        double total = subtotal + tax + shipping;

        order newOrder = new order("ORD" + System.currentTimeMillis(), items, subtotal, tax, shipping, total, "Guest User", "", "", "VISA ****1234", System.currentTimeMillis(), "Pending");

        cartViewModel.clearCart();

        Intent intent = new Intent(this, OrderConfirmationActivity.class);
        intent.putExtra("order", new Gson().toJson(newOrder));
        startActivity(intent);
        finish();
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
