package com.codepath.socialshopper.socialshopper.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.R;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gumapathi on 10/15/2017.
 */

public class AddItemDetailsDialogFragment extends DialogFragment implements View.OnClickListener {
    public final String TAG = "SocShpAdItm";
    @BindView(R.id.etItemName) EditText etItemName;
    @BindView(R.id.etItemBrand) EditText etItemBrand;
    @BindView(R.id.etItemQty) EditText etItemQty;
    @BindView(R.id.btnAddItem) Button btnAddItem;
    ShoppableItem mShoppableItem;

    public interface AddItemDetailsDialogListener {
        void onFinishAddItemDetailsDialog(Bundle bundle);
    }

    public AddItemDetailsDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddItemDetailsDialogFragment newInstance(ShoppableItem shoppableItem) {
        AddItemDetailsDialogFragment frag = new AddItemDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("item", Parcels.wrap(shoppableItem));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_add_item_details, container);
        ButterKnife.bind(this,view);
        mShoppableItem = Parcels.unwrap(getArguments().getParcelable("item"));
        etItemName.setText(mShoppableItem.getmItemName());
        etItemBrand.setText(mShoppableItem.getmItemBrand());
        etItemQty.setText(mShoppableItem.getmItemQty());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        btnAddItem.setOnClickListener(this);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onclick");
        Toast.makeText(v.getContext(),"clicked",Toast.LENGTH_LONG).show();
        final Bundle bundle = new Bundle();
        bundle.putParcelable("AddedItem", Parcels.wrap(mShoppableItem));
        final AddItemDetailsDialogListener listener = (AddItemDetailsDialogListener) getActivity(); //getTargetFragment();//
        listener.onFinishAddItemDetailsDialog(bundle);
        Log.i(TAG, "added to bundle");
        Log.i(TAG, "returning via listener");
        dismiss();
    }
}
