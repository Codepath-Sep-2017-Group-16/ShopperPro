package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import static com.codepath.socialshopper.socialshopper.Activities.ChooseStoreActivity.shoppingList;

public class MainActivity extends AppCompatActivity implements
        DatabaseUtils.OnActiveListsFetchListener, DatabaseUtils.OnListFetchListener, AddItemDetailsDialogFragment.AddItemDetailsDialogListener {
    public final String TAG = "SocShpMainAct";
    private DatabaseUtils databaseUtils;
    @BindView(R.id.nvView) NavigationView nvDrawer;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        databaseUtils = new DatabaseUtils(this);
        setUpToolBar();
        setUpInitialScreen();
        shoppingList.setListId(CommonUtils.getUuid());
        setupDrawerContent(nvDrawer);

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
        if(id == R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        switch(menuItem.getItemId()) {
            case R.id.nav_choose_store:
                Toast.makeText(this, "Choose store clicked", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Choose store clicked");
                break;
            case R.id.nav_old_orders:
                Toast.makeText(this, "Old orders clicked", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Old orders clicked");
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Settings clicked");
                break;
            default:
        }

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
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
