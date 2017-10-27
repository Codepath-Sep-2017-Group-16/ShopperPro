package com.codepath.socialshopper.socialshopper.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.socialshopper.socialshopper.Activities.MainActivity;
import com.codepath.socialshopper.socialshopper.Fragments.AddItemDetailsDialogFragment;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by gumapathi on 10/11/2017.
 */

public class ShoppableItemsArrayAdapter extends RecyclerView.Adapter<ShoppableItemsArrayAdapter.ShoppableItemsViewHolder> {
    Context mContext;
    ArrayList<ShoppableItem> shoppableItems;
    public final String TAG = "SocShpAdap";

    public ShoppableItemsArrayAdapter(ArrayList<ShoppableItem> shoppableItems) {
        this.shoppableItems = shoppableItems;
    }

    @Override
    public ShoppableItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_shoppable,parent,false);
        ShoppableItemsViewHolder viewHolder = new ShoppableItemsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ShoppableItemsViewHolder holder, int position) {
        final ShoppableItem shoppableItem = shoppableItems.get(position);
        Log.i(TAG, "onBindViewHolder");

        holder.btnShoppableItem.setText(shoppableItem.getmItemName());
        Drawable top = ContextCompat.getDrawable(mContext,mContext.getResources().getIdentifier(shoppableItem.getmItemIconFileName(),"raw",mContext.getPackageName()));
        holder.btnShoppableItem.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
        holder.btnShoppableItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.showAddOptions();

                Log.i(TAG, "added onClick");
                MainActivity mainActivity = (MainActivity) v.getContext();
                AddItemDetailsDialogFragment fragment;
                FragmentManager fm = mainActivity.getSupportFragmentManager();
                fragment = AddItemDetailsDialogFragment.newInstance(shoppableItem);
                fragment.setTargetFragment(fragment,20);
                fragment.show(fm, "fragment_alert");

            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppableItems.size();
    }

    public class ShoppableItemsViewHolder extends RecyclerView.ViewHolder {
        Button btnShoppableItem;
        ImageView ivAdd;
        ImageView ivDelete;
        TextView tvAmount;

        public ShoppableItemsViewHolder(View itemView) {
            super(itemView);
            btnShoppableItem = (Button) itemView.findViewById(R.id.btnShoppableItem);
            ivAdd = (ImageView) itemView.findViewById(R.id.ivAdd);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);

        }

        private void showAddOptions() {
            ivAdd.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);
        }
    }
}
