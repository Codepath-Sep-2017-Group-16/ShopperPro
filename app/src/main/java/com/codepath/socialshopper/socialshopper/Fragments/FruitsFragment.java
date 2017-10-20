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

public class FruitsFragment extends ItemsFragment {
    public final String TAG = "SocShpHorFrag";


    public FruitsFragment() {
    }

    public static FruitsFragment newInstance(@Nullable Bundle savedInstanceState) {
        FruitsFragment fragment = new FruitsFragment();
        fragment.setArguments(savedInstanceState);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            this.setArguments(savedInstanceState);
        }
    }

    public void populateDefaultItems() {
        Log.i(TAG, "populateDefaultItems");
        ShoppableItem itemBanana = new ShoppableItem(CommonUtils.getUuid(), "Banana", "@drawable/banana_file","1lb","DelMonte");
        shoppableItems.add(itemBanana);

        ShoppableItem itemApple = new ShoppableItem(CommonUtils.getUuid(), "Apple", "@drawable/apple_file","1lb","DelMonte");
        shoppableItems.add(itemApple);

        ShoppableItem itemLemon = new ShoppableItem(CommonUtils.getUuid(), "Lemon", "@drawable/lemon_file","1lb","DelMonte");
        shoppableItems.add(itemLemon);

        ShoppableItem itemStrawberry = new ShoppableItem(CommonUtils.getUuid(), "Strawberry", "@drawable/strawberry_file","1lb","DelMonte");
        shoppableItems.add(itemStrawberry);

        ShoppableItem itemBanana1 = new ShoppableItem(CommonUtils.getUuid(), "Banana", "@drawable/banana_file","1lb","DelMonte");
        shoppableItems.add(itemBanana1);

        ShoppableItem itemApple2 = new ShoppableItem(CommonUtils.getUuid(), "Apple", "@drawable/apple_file","1lb","DelMonte");
        shoppableItems.add(itemApple2);

        ShoppableItem itemLemon3 = new ShoppableItem(CommonUtils.getUuid(), "Lemon", "@drawable/lemon_file","1lb","DelMonte");
        shoppableItems.add(itemLemon3);

        ShoppableItem itemStrawberry4 = new ShoppableItem(CommonUtils.getUuid(), "Strawberry", "@drawable/strawberry_file","1lb","DelMonte");
        shoppableItems.add(itemStrawberry4);

        adapter.notifyDataSetChanged();
    }
}
