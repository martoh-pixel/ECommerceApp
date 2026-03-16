package com.example.ecommerceapp.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.ecommerceapp.cartItem;
import com.example.ecommerceapp.product;
import com.example.ecommerceapp.repositories.CartRepository;
import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private CartRepository repository;
    private LiveData<List<cartItem>> cartItems;
    private LiveData<Double> cartTotal;
    private MutableLiveData<Integer> checkoutProgress = new MutableLiveData<>(1);

    public CartViewModel(Application application) {
        super(application);
        repository = new CartRepository(application);
        cartItems = repository.getAllCartItems();
        cartTotal = repository.getCartTotal();
    }

    public LiveData<List<cartItem>> getCartItems() { return cartItems; }
    public LiveData<Double> getCartTotal() { return cartTotal; }
    public LiveData<Integer> getCheckoutProgress() { return checkoutProgress; }

    public void addToCart(product productItem, int quantity) {
        cartItem item = new cartItem(productItem.getId(), productItem.getName(),
                productItem.getPrice(), quantity, productItem.getImageUrl());
        repository.addToCart(item);
    }

    public void updateItemQuantity(cartItem item, int newQuantity) {
        if (newQuantity <= 0) repository.removeFromCart(item);
        else {
            repository.updateQuantity(item, newQuantity);
        }
    }

    public void removeItem(cartItem item) { repository.removeFromCart(item); }
    public void setCheckoutStep(int step) { checkoutProgress.setValue(step); }
    public void clearCart() { repository.clearCart(); }
}
