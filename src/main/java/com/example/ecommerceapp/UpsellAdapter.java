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
import java.util.List;
import java.util.Locale;

public class UpsellAdapter extends RecyclerView.Adapter<UpsellAdapter.UpsellViewHolder> {

    private List<product> products;
    private OnUpsellAddListener listener;

    public interface OnUpsellAddListener {
        void onAddToCart(product product);
    }

    public UpsellAdapter(List<product> products, OnUpsellAddListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UpsellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Assuming activity_upsell_adapter.xml is the item layout based on file list
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_upsell_adapter, parent, false);
        return new UpsellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpsellViewHolder holder, int position) {
        product product = products.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class UpsellViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;
        Button addButton;

        UpsellViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.upsellImage);
            productName = itemView.findViewById(R.id.upsellName);
            productPrice = itemView.findViewById(R.id.upsellPrice);
            addButton = itemView.findViewById(R.id.addUpsellBtn);
        }

        void bind(final product product, final OnUpsellAddListener listener) {
            productName.setText(product.getName());
            productPrice.setText(String.format(Locale.US, "$%.2f", product.getPrice()));

            Glide.with(itemView.getContext())
                    .load(product.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(productImage);

            addButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddToCart(product);
                }
            });
        }
    }
}
