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

public class DairyFragment extends ItemsFragment {
    public final String TAG = "SocShpHorFrag";


    public DairyFragment() {
    }

    public static DairyFragment newInstance(@Nullable Bundle savedInstanceState) {
        DairyFragment fragment = new DairyFragment();
        fragment.setArguments(savedInstanceState);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public  void populateDefaultItems() {
        Log.i(TAG, "populateDefaultItems");
        ShoppableItem itemMilk = new ShoppableItem(CommonUtils.getUuid(), "Milk", "@drawable/milk_file","1gal","Horizon");
        shoppableItems.add(itemMilk);

        ShoppableItem itemCheese = new ShoppableItem(CommonUtils.getUuid(), "Cheese", "@drawable/cheese_file","7oz","Cheddar");
        shoppableItems.add(itemCheese);

        ShoppableItem itemYogurt = new ShoppableItem(CommonUtils.getUuid(), "Yogurt", "@drawable/yogurt_file","1gal","Noosa");
        shoppableItems.add(itemYogurt);

        ShoppableItem itemAlmondMilk = new ShoppableItem(CommonUtils.getUuid(), "AlmondMilk", "@drawable/almond_file","2gal","Silk");
        shoppableItems.add(itemAlmondMilk);

        ShoppableItem itemMilk2 = new ShoppableItem(CommonUtils.getUuid(), "Milk", "@drawable/milk_file","1gal","Horizon");
        shoppableItems.add(itemMilk2);

        ShoppableItem itemCheese3 = new ShoppableItem(CommonUtils.getUuid(), "Cheese", "@drawable/cheese_file","7oz","Cheddar");
        shoppableItems.add(itemCheese3);

        ShoppableItem itemYogurt4 = new ShoppableItem(CommonUtils.getUuid(), "Yogurt", "@drawable/yogurt_file","1gal","Noosa");
        shoppableItems.add(itemYogurt4);

        ShoppableItem itemAlmondMilk5 = new ShoppableItem(CommonUtils.getUuid(), "AlmondMilk", "@drawable/almond_file","2gal","Silk");
        shoppableItems.add(itemAlmondMilk5);

        adapter.notifyDataSetChanged();
    }
}