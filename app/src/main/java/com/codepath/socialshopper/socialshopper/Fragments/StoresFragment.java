package com.codepath.socialshopper.socialshopper.Fragments;

import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codepath.socialshopper.socialshopper.Adapters.StoresArrayAdapter;
import com.codepath.socialshopper.socialshopper.Models.Store;
import com.codepath.socialshopper.socialshopper.R;

import java.util.ArrayList;

public class StoresFragment extends Fragment {
    ArrayList<Store> stores;
    StoresArrayAdapter adapter;
    RecyclerView rvStores;
    public final String TAG = "StoresFragment";

    private OnStoreFragmentInteractionListener mListener;

    public StoresFragment() {
        // Required empty public constructor
    }

    public static StoresFragment newInstance() {
        StoresFragment fragment = new StoresFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        stores = new ArrayList<>();
        adapter = new StoresArrayAdapter(stores);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stores, container, false);
        rvStores = (RecyclerView) view.findViewById(R.id.rvStores);
        stores = new ArrayList<>();

        adapter = new StoresArrayAdapter(stores);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvStores.setLayoutManager(linearLayoutManager);
        rvStores.setAdapter(adapter);
        populateStores();
        return view;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onStoreFragmentInteraction("store name");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStoreFragmentInteractionListener) {
            mListener = (OnStoreFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void populateStores() {
        Log.d(TAG, "Populating Stores");
        Store storeCostco = new Store("Costco", R.drawable.storelogo_costco);
        Store storeWalmart = new Store("Walmart", R.drawable.storelogo_walmart);
        Store storeWf = new Store("Whole Foods", R.drawable.storelogo_wholefoods);
        Store storeTarget = new Store("Target", R.drawable.storelogo_target);
        Store storeSafeway = new Store("Safeway", R.drawable.storelogo_safeway);
        Store storeLucky = new Store("Lucky", R.drawable.storelogo_lucky);

        stores.add(storeCostco);
        stores.add(storeWalmart);
        stores.add(storeTarget);
        stores.add(storeWf);
        stores.add(storeSafeway);
        stores.add(storeLucky);
        adapter.notifyDataSetChanged();
    }


    public interface OnStoreFragmentInteractionListener {
        void onStoreFragmentInteraction(String storeName);
    }
}
