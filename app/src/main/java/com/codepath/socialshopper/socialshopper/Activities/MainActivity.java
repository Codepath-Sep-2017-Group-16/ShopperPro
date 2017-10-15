package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Fragments.AddItemDetailsDialogFragment;
import com.codepath.socialshopper.socialshopper.Fragments.HorizontalItemsFragment;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.CommonUtils;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.FacebookUtils;
import com.crashlytics.android.Crashlytics;

import org.parceler.Parcels;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements
        DatabaseUtils.OnActiveListsFetchListener, DatabaseUtils.OnListFetchListener, AddItemDetailsDialogFragment.AddItemDetailsDialogListener {
    public final String TAG = "SocShpMainAct";
    private DatabaseUtils databaseUtils;
    static ShoppingList shoppingList = new ShoppingList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        databaseUtils = new DatabaseUtils(this);
        processIntentAction(getIntent());
        setUpToolBar();
        setUpInitialScreen();
        shoppingList.setListId(CommonUtils.getUuid());
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_menu_black_24dp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpInitialScreen() {
        Log.i(TAG, "starting initial set-up");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flItems, new HorizontalItemsFragment());
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "inflatingmenu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_view_shoppinglist) {
            Toast.makeText(this, "action_view_shoppinglist", Toast.LENGTH_LONG).show();
            Log.i(TAG, "action_view_shoppinglist");
            Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onNewIntent(Intent intent) {
        processIntentAction(intent);
        super.onNewIntent(intent);
    }

    private void processIntentAction(Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case "YES_ACTION":
                    Toast.makeText(this, "Yes :)", Toast.LENGTH_SHORT).show();
                    break;
                case "NO_ACTION":
                    Toast.makeText(this, "No :(", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onFinishAddItemDetailsDialog(Bundle bundle) {
        if (bundle != null) {
            ShoppableItem item = (ShoppableItem) Parcels.unwrap(bundle.getParcelable("AddedItem"));
            Log.i(TAG, "from bundle");
            shoppingList.addItems(item);
            Log.i(TAG, " added to shopping list");
            Log.i(TAG, item.getmItemBrand());
            Log.i(TAG, item.getmItemName());
            Log.i(TAG, item.getmItemQty());
        }
    }
}
