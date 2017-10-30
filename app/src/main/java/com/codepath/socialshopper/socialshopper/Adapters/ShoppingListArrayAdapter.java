package com.codepath.socialshopper.socialshopper.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.R;

import java.util.ArrayList;

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
        View view = inflater.inflate(R.layout.item_shopping_list,parent,false);
        ShoppingListViewHolder viewHolder = new ShoppingListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShoppingListViewHolder holder, int position) {
        final ShoppableItem shoppableItem = shoppableItems.get(position);
        holder.tvItemQty.setText(Integer.toString(shoppableItem.getmItemQty()));
        holder.tvItemName.setText(shoppableItem.getmItemName());
        holder.tvItemBrand.setText(shoppableItem.getmItemBrand());


        String icon = shoppableItem.getmItemIconFileName().isEmpty() ? "" : shoppableItem.getmItemIconFileName();
        int id = mContext.getResources().getIdentifier(icon, "drawable", mContext.getPackageName());
        Drawable drawable = mContext.getResources().getDrawable(id);

        holder.ivItemImage.setImageDrawable(drawable);
        Log.i(TAG, "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return shoppableItems.size();
    }

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemQty;
        TextView tvItemName;
        TextView tvItemBrand;
        ImageView ivItemImage;
        public ShoppingListViewHolder(View itemView) {
            super(itemView);
            tvItemBrand = (TextView) itemView.findViewById(R.id.tvItemBrandList);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemNameList);
            tvItemQty = (TextView) itemView.findViewById(R.id.tvItemQtyList);
            ivItemImage = (ImageView) itemView.findViewById(R.id.ivItemImage);
        }
    }
}
