package com.codepath.socialshopper.socialshopper.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.Store;
import com.codepath.socialshopper.socialshopper.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by rdeshpan on 10/25/2017.
 */

public class StoresArrayAdapter extends RecyclerView.Adapter<StoresArrayAdapter.StoreViewHolder> {
    Context mContext;
    ArrayList<Store> stores;
    public final String TAG = "StoresArrayAdapter";

    public StoresArrayAdapter(ArrayList<Store> stores) {
        this.stores = stores;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_store,parent,false);
        StoreViewHolder viewHolder = new StoreViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        final Store store = stores.get(position);
        holder.Bind(store);
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStoreImage;
        Store store;

        public StoreViewHolder(View itemView) {
            super(itemView);
            ivStoreImage = (ImageView) itemView.findViewById(R.id.ivStoreImage);
            ivStoreImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, store.getName());
                }
            });
        }

        public void Bind(Store store) {
            this.store = store;
            ivStoreImage.setImageResource(store.getimageResource());
        }
    }
}
