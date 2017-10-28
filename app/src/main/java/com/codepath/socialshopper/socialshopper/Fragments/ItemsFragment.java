package com.codepath.socialshopper.socialshopper.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.socialshopper.socialshopper.Adapters.ShoppableItemsArrayAdapter;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by gumapathi on 10/11/2017.
 */

public abstract class ItemsFragment extends Fragment {
    ArrayList<ShoppableItem> shoppableItems;
    ShoppableItemsArrayAdapter adapter;
    RecyclerView rvItems;
    public final String TAG = "SocShpHorFrag";


    public ItemsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            this.setArguments(savedInstanceState);
        }
        shoppableItems = new ArrayList<>();
        adapter = new ShoppableItemsArrayAdapter(shoppableItems);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        rvItems = (RecyclerView) view.findViewById(R.id.rvItems);
        shoppableItems = new ArrayList<>();

        adapter = new ShoppableItemsArrayAdapter(shoppableItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvItems.setLayoutManager(linearLayoutManager);
        rvItems.setAdapter(adapter);
        populateDefaultItems();
        adapter.setActivity(getActivity());
        return view;
    }

    abstract void populateDefaultItems();
}
