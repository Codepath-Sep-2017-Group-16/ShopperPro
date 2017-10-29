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

public class VegetableFragment extends ItemsFragment {
    public final String TAG = "SocShpHorFrag";


    public VegetableFragment() {
    }

    public static VegetableFragment newInstance(@Nullable Bundle savedInstanceState) {
        VegetableFragment fragment = new VegetableFragment();
        fragment.setArguments(savedInstanceState);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public  void populateDefaultItems() {
        Log.i(TAG, "populateDefaultItems");
        ShoppableItem itemCarrot = new ShoppableItem(CommonUtils.getUuid(), "Carrot", "@drawable/carrot_file",0,"DelMonte","lbs");
        shoppableItems.add(itemCarrot);

        ShoppableItem itemTomato = new ShoppableItem(CommonUtils.getUuid(), "Tomato", "@drawable/tomato_file",0,"DelMonte","lbs");
        shoppableItems.add(itemTomato);

        ShoppableItem itemCauliflower = new ShoppableItem(CommonUtils.getUuid(), "Cauliflower", "@drawable/cauliflower_file",0,"DelMonte","lbs");
        shoppableItems.add(itemCauliflower);

        ShoppableItem itemLettuce = new ShoppableItem(CommonUtils.getUuid(), "Lettuce", "@drawable/lettuce_file",0,"DelMonte","lbs");
        shoppableItems.add(itemLettuce);

        ShoppableItem itemCarrot1 = new ShoppableItem(CommonUtils.getUuid(), "Carrot", "@drawable/carrot_file",0,"DelMonte","lbs");
        shoppableItems.add(itemCarrot1);

        ShoppableItem itemTomato2 = new ShoppableItem(CommonUtils.getUuid(), "Tomato", "@drawable/tomato_file",0,"DelMonte","lbs");
        shoppableItems.add(itemTomato2);

        ShoppableItem itemCauliflower3 = new ShoppableItem(CommonUtils.getUuid(), "Cauliflower", "@drawable/cauliflower_file",0,"DelMonte","lbs");
        shoppableItems.add(itemCauliflower3);

        ShoppableItem itemLettuce4 = new ShoppableItem(CommonUtils.getUuid(), "Lettuce", "@drawable/lettuce_file",0,"DelMonte","lbs");
        shoppableItems.add(itemLettuce4);

        adapter.notifyDataSetChanged();
    }
}
