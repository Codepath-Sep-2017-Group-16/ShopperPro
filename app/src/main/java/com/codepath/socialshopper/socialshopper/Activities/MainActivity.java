package com.codepath.socialshopper.socialshopper.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.socialshopper.socialshopper.Fragments.HorizontalItemsFragment;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.CommonUtils;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.FacebookUtils;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements DatabaseUtils.OnActiveListsFetchListener, DatabaseUtils.OnListFetchListener {
    public final String TAG = "SocShpMainAct";
    private DatabaseUtils databaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        databaseUtils = new DatabaseUtils(this);

        setUpInitialScreen();
    }

    private void setUpInitialScreen() {
        Log.i(TAG, "starting initial set-up");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flItems, new HorizontalItemsFragment());
        ft.commit();
    }

    private void saveList(ShoppingList shoppingList) {
        DatabaseUtils.saveList(FacebookUtils.getFacebookId(), shoppingList);
    }

    private void getActiveLists() {
        databaseUtils.getActiveLists(this, FacebookUtils.getFacebookId());
    }

    private void getListByListId(String listId) {
        databaseUtils.getShoppingListByListId(listId);
    }

    @Override
    public void OnListsFetchListener(ArrayList<ShoppingList> shoppingLists) {
        // Update UI here
    }

    @Override
    public void OnListFetchListener(ShoppingList shoppingList) {
        // Update UI here
    }

    /*
    private void SaveItemSample() {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setListId(CommonUtils.getUuid());
        shoppingList.setItems(new ArrayList<ShoppableItem>());

        ShoppableItem shoppableItem = new ShoppableItem();
        shoppableItem.setItemId(CommonUtils.getUuid());
        shoppableItem.setmItemIconFileName("TestItem1");
        shoppingList.getItems().add(shoppableItem);

        shoppableItem = new ShoppableItem();
        shoppableItem.setItemId(CommonUtils.getUuid());
        shoppableItem.setmItemIconFileName("TestItem2");
        shoppingList.getItems().add(shoppableItem);

        saveList(shoppingList);
        getActiveLists();
    }
    */
}
