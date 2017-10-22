package com.codepath.socialshopper.socialshopper.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saripirala on 10/15/17.
 */

public class ShoppersListArrayAdapter  extends RecyclerView.Adapter<ShoppersListArrayAdapter.ViewHolder>{

    private ArrayList<ShoppableItem> list;
    private Context context;


    public ShoppersListArrayAdapter(Context context, ArrayList<ShoppableItem> list)
    {
        this.context = context;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ShoppableItem item = list.get(position);

        holder.getIvItemImage().setImageResource(R.drawable.apple);

        if(item.getmItemIconFileName().contains("banana"))
            holder.getIvItemImage().setImageResource(R.drawable.banana);
        else if(item.getmItemIconFileName().contains("apple")){
            holder.getIvItemImage().setImageResource(R.drawable.apple);
        }else if(item.getmItemIconFileName().contains("lemon")){
            holder.getIvItemImage().setImageResource(R.drawable.lemon);
        }

        holder.getTvItemBrand().setText(item.getmItemBrand());
        holder.getTvItemName().setText(item.getmItemName());
        holder.getTvItemQuantity().setText(item.getmItemQty());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView getTvItemName() {
            return tvItemName;
        }

        public TextView getTvItemBrand() {
            return tvItemBrand;
        }

        public TextView getTvItemQuantity() {
            return tvItemQuantity;
        }

        public ImageView getIvItemImage() {
            return ivItemImage;
        }

        private TextView tvItemName;
        private TextView tvItemBrand;
        private TextView tvItemQuantity;
        private ImageView ivItemImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvItemQuantity = (TextView) itemView.findViewById(R.id.tvItemQuantity);
            tvItemBrand = (TextView) itemView.findViewById(R.id.tvItemBrand);
            ivItemImage = (ImageView) itemView.findViewById(R.id.ivItemImage);
        }
    }


}
