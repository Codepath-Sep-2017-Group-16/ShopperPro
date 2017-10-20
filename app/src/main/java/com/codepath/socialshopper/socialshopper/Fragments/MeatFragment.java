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

public class MeatFragment extends ItemsFragment {
    public final String TAG = "SocShpHorFrag";


    public MeatFragment() {
    }

    public static MeatFragment newInstance(@Nullable Bundle savedInstanceState) {
        MeatFragment fragment = new MeatFragment();
        fragment.setArguments(savedInstanceState);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void populateDefaultItems() {
        Log.i(TAG, "populateDefaultItems");
        ShoppableItem itemSteak = new ShoppableItem(CommonUtils.getUuid(), "Steak Grill Pack", "@drawable/steak_file","1lb","D'Artagnan");
        shoppableItems.add(itemSteak);

        ShoppableItem itemRibs = new ShoppableItem(CommonUtils.getUuid(), "Short Ribs", "@drawable/ribs_file","1lb","D'Artagnan");
        shoppableItems.add(itemRibs);

        ShoppableItem itemFilet = new ShoppableItem(CommonUtils.getUuid(), "Angus Filet Mignon", "@drawable/filet_file","1oz","Rastelli USDA");
        shoppableItems.add(itemFilet);

        ShoppableItem itemDuck = new ShoppableItem(CommonUtils.getUuid(), "Duck", "@drawable/duck_file","1lb","D'Artagnans");
        shoppableItems.add(itemDuck);

        ShoppableItem itemSteak1 = new ShoppableItem(CommonUtils.getUuid(), "Steak Grill Pack", "@drawable/steak_file","1lb","D'Artagnan");
        shoppableItems.add(itemSteak1);

        ShoppableItem itemRibs1 = new ShoppableItem(CommonUtils.getUuid(), "Short Ribs", "@drawable/ribs_file","1lb","D'Artagnan");
        shoppableItems.add(itemRibs1);

        ShoppableItem itemFilet1 = new ShoppableItem(CommonUtils.getUuid(), "Angus Filet Mignon", "@drawable/filet_file","1oz","Rastelli USDA");
        shoppableItems.add(itemFilet1);

        ShoppableItem itemDuck1 = new ShoppableItem(CommonUtils.getUuid(), "Duck", "@drawable/duck_file","1lb","D'Artagnans");
        shoppableItems.add(itemDuck1);

        adapter.notifyDataSetChanged();
    }
}
