package com.codepath.socialshopper.socialshopper.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.R;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

/**
 * Created by gumapathi on 10/11/2017.
 */

public class ShoppingListArrayAdapter extends RecyclerView.Adapter<ShoppingListArrayAdapter.ShoppingListViewHolder> {
    Context mContext;
    ArrayList<ShoppableItem> shoppableItems;
    public final String TAG = "SocShpLisAdap";

    public ShoppingListArrayAdapter(ArrayList<ShoppableItem> shoppableItems) {
        this.shoppableItems = shoppableItems;
    }

    @Override
    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_shopping_list, parent, false);
        ShoppingListViewHolder viewHolder = new ShoppingListViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ShoppingListViewHolder holder, int position) {
        final ShoppableItem item = shoppableItems.get(position);
        holder.Bind(item);

        String icon = item.getmItemIconFileName().isEmpty() ? "" : item.getmItemIconFileName();
        int id = mContext.getResources().getIdentifier(icon, "drawable", mContext.getPackageName());
        Drawable drawable = mContext.getResources().getDrawable(id);

        holder.ivItemImage.setImageDrawable(drawable);

        holder.tvItemBrand.setText(item.getmItemBrand());
        holder.tvItemName.setText(item.getmItemName());
        holder.tvItemQuantity.setText(Integer.toString(item.getmItemQty()));
        holder.tvItemMeasure.setText(item.getItemMeasure());

        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.updateQuantity(item.getmItemQty() + 1, view);
                notifyDataSetChanged();
            }
        });

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.updateQuantity(item.getmItemQty() - 1, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppableItems.size();
    }

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvItemBrand;
        TextView tvItemQuantity;
        TextView tvItemMeasure;
        ImageView ivItemImage;
        ImageView ivAdd;
        ImageView ivRemove;
        ShoppableItem shoppableItem;

        public ShoppingListViewHolder(View itemView) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvItemQuantity = (TextView) itemView.findViewById(R.id.tvItemQuantity);
            tvItemMeasure = (TextView) itemView.findViewById(R.id.tvItemMeasure);
            tvItemBrand = (TextView) itemView.findViewById(R.id.tvItemBrand);
            ivItemImage = (ImageView) itemView.findViewById(R.id.ivItemImage);
            ivAdd = (ImageView) itemView.findViewById(R.id.ivAdd);
            ivRemove = (ImageView) itemView.findViewById(R.id.ivRemove);
        }

        public void Bind(ShoppableItem shoppableItem) {
            this.shoppableItem = shoppableItem;
        }

        public void updateQuantity(final int quantity, View view) {
            final ObjectAnimator addAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 1, 0);
            addAnim.setDuration(200);
            addAnim.setRepeatMode(ValueAnimator.REVERSE);
            addAnim.setRepeatCount(1);
            addAnim.start();
            addAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    updateQuantity(quantity);
                    notifyDataSetChanged();
                }
            });
        }

        private void updateQuantity(int quantity) {

            this.shoppableItem.setmItemQty(quantity);
        }
    }
}
