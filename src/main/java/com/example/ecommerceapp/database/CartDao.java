package com.example.ecommerceapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.ecommerceapp.cartItem;
import java.util.List;

@Dao
public interface CartDao {

    @Query("SELECT * FROM cart_items")
    LiveData<List<cartItem>> getAllCartItems();

    @Query("SELECT * FROM cart_items")
    List<cartItem> getAllCartItemsSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCartItem(cartItem item);

    @Update
    void updateCartItem(cartItem item);

    @Delete
    void deleteCartItem(cartItem item);

    @Query("DELETE FROM cart_items")
    void clearCart();

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    cartItem getCartItemByProductId(String productId);

    @Query("SELECT SUM(price * quantity) FROM cart_items")
    LiveData<Double> getCartTotal();
}
