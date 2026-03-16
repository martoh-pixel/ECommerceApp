package com.example.ecommerceapp.repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.ecommerceapp.cartItem;
import com.example.ecommerceapp.database.AppDatabase;
import com.example.ecommerceapp.database.CartDao;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {
    private CartDao cartDao;
    private LiveData<List<cartItem>> allCartItems;
    private LiveData<Double> cartTotal;
    private ExecutorService executorService;

    public CartRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        cartDao = database.cartDao();
        allCartItems = cartDao.getAllCartItems();
        cartTotal = cartDao.getCartTotal();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<cartItem>> getAllCartItems() { return allCartItems; }
    public LiveData<Double> getCartTotal() { return cartTotal; }

    public void addToCart(cartItem item) {
        executorService.execute(() -> {
            cartItem existing = cartDao.getCartItemByProductId(item.getProductId());
            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                cartDao.updateCartItem(existing);
            } else {
                cartDao.insertCartItem(item);
            }
        });
    }

    public void updateQuantity(cartItem item, int newQuantity) {
        executorService.execute(() -> {
            item.setQuantity(newQuantity);
            cartDao.updateCartItem(item);
        });
    }

    public void removeFromCart(cartItem item) {
        executorService.execute(() -> cartDao.deleteCartItem(item));
    }

    public void clearCart() {
        executorService.execute(() -> cartDao.clearCart());
    }
}
