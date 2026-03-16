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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<product> productList;
    private OnAddToCartClickListener listener;

    public interface OnAddToCartClickListener {
        void onAddToCartClick(product productItem);
    }

    public ProductAdapter(List<product> productList, OnAddToCartClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use activity_product.xml as the item layout since it contains the actual product view elements
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        product productItem = productList.get(position);
        holder.bind(productItem, listener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;
        Button addToCartBtn;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }

        void bind(final product productItem, final OnAddToCartClickListener listener) {
            productName.setText(productItem.getName());
            productPrice.setText(String.format(Locale.US, "$%.2f", productItem.getPrice()));

            // Set placeholder and image from drawable or URL
            Glide.with(itemView.getContext())
                    .load(productItem.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.product1) // Fallback to your jpeg
                    .into(productImage);

            addToCartBtn.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddToCartClick(productItem);
                }
            });
        }
    }
}
