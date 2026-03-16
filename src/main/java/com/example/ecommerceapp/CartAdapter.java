package com.example.ecommerceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<cartItem> cartItems = new ArrayList<>();
    private OnQuantityChangeListener quantityChangeListener;
    private OnRemoveClickListener removeClickListener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged(cartItem item, int newQuantity);
    }

    public interface OnRemoveClickListener {
        void onRemoveClicked(cartItem item);
    }

    public CartAdapter(OnQuantityChangeListener quantityChangeListener,
                       OnRemoveClickListener removeClickListener) {
        this.quantityChangeListener = quantityChangeListener;
        this.removeClickListener = removeClickListener;
    }

    public void submitList(List<cartItem> items) {
        this.cartItems = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Corrected to use activity_cart_item.xml which has the item views defined
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        cartItem item = cartItems.get(position);
        holder.bind(item, quantityChangeListener, removeClickListener);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemPrice, quantityText, itemTotal;
        Button decreaseBtn, increaseBtn;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cartItemImage);
            itemName = itemView.findViewById(R.id.cartItemName);
            itemPrice = itemView.findViewById(R.id.cartItemPrice);
            quantityText = itemView.findViewById(R.id.quantityTv);
            itemTotal = itemView.findViewById(R.id.itemTotal);
            decreaseBtn = itemView.findViewById(R.id.decreaseBtn);
            increaseBtn = itemView.findViewById(R.id.increaseBtn);
        }

        void bind(final cartItem item,
                  final OnQuantityChangeListener quantityListener,
                  final OnRemoveClickListener removeListener) {

            itemName.setText(item.getProductName());
            itemPrice.setText(String.format(Locale.US, "$%.2f", item.getPrice()));
            quantityText.setText(String.valueOf(item.getQuantity()));
            itemTotal.setText(String.format(Locale.US, "$%.2f", item.getTotalPrice()));

            Glide.with(itemView.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(itemImage);

            if (decreaseBtn != null) {
                decreaseBtn.setOnClickListener(v -> {
                    int newQuantity = item.getQuantity() - 1;
                    if (newQuantity <= 0) {
                        if (removeListener != null) removeListener.onRemoveClicked(item);
                    } else {
                        if (quantityListener != null) quantityListener.onQuantityChanged(item, newQuantity);
                    }
                });
            }

            if (increaseBtn != null) {
                increaseBtn.setOnClickListener(v -> {
                    int newQuantity = item.getQuantity() + 1;
                    if (quantityListener != null) quantityListener.onQuantityChanged(item, newQuantity);
                });
            }

            itemView.setOnLongClickListener(v -> {
                if (removeListener != null) {
                    removeListener.onRemoveClicked(item);
                    return true;
                }
                return false;
            });
        }
    }
}
