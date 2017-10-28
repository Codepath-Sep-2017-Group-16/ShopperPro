package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Adapters.ShoppableItemsArrayAdapter;
import com.codepath.socialshopper.socialshopper.Fragments.AddItemDetailsDialogFragment;
import com.codepath.socialshopper.socialshopper.Fragments.DairyFragment;
import com.codepath.socialshopper.socialshopper.Fragments.FruitsFragment;
import com.codepath.socialshopper.socialshopper.Fragments.MeatFragment;
import com.codepath.socialshopper.socialshopper.Fragments.StoresFragment;
import com.codepath.socialshopper.socialshopper.Fragments.VegetableFragment;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.CommonUtils;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.FacebookUtils;
import com.codepath.socialshopper.socialshopper.Utils.LocationUtils;
import com.crashlytics.android.Crashlytics;
import com.facebook.Profile;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements
        DatabaseUtils.OnActiveListsFetchListener, DatabaseUtils.OnListFetchListener, AddItemDetailsDialogFragment.AddItemDetailsDialogListener,
        LocationUtils.OnLocationFetchListener, StoresFragment.OnStoreFragmentInteractionListener , ShoppableItemsArrayAdapter.OnAddItemListener{

    public final String TAG = "SocShpMainAct";
    private DatabaseUtils databaseUtils;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private LocationUtils locationUtils;
    @BindView(R.id.nvView)
    NavigationView nvDrawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView((R.id.toolbar))
    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    TextView tvCartCount;
    ImageView ivCart;

    static ShoppingList shoppingList = new ShoppingList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        databaseUtils = new DatabaseUtils();
        locationUtils = new LocationUtils();
        locationUtils.initializePlaces(this);
        setUpToolBar();
        setUpInitialScreen();

        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        initializeShoppingList();
        initializeCart();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setUpToolBar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_menu));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void setUpInitialScreen() {
        Log.i(TAG, "starting initial set-up");
        FragmentTransaction ftFruit = getSupportFragmentManager().beginTransaction();
        ftFruit.replace(R.id.fruitFragment, new FruitsFragment());
        ftFruit.commit();

        FragmentTransaction ftVege = getSupportFragmentManager().beginTransaction();
        ftVege.replace(R.id.vegeFragment, new VegetableFragment());
        ftVege.commit();

        FragmentTransaction ftDairy = getSupportFragmentManager().beginTransaction();
        ftDairy.replace(R.id.dairyFragment, new DairyFragment());
        ftDairy.commit();

        FragmentTransaction ftMeat = getSupportFragmentManager().beginTransaction();
        ftMeat.replace(R.id.meatFragment, new MeatFragment());
        ftMeat.commit();

        FragmentTransaction ftStore = getSupportFragmentManager().beginTransaction();
        ftStore.replace(R.id.storeFragment, new StoresFragment());
        ftStore.commit();
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

            if(shoppingList==null ){
                MDToast mdToast = MDToast.makeText(this, "Your cart is empty !", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                mdToast.show();
                return true;
            }
            if(shoppingList.getStore()==null){
                MDToast mdToast = MDToast.makeText(this, "Please choose a store", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                mdToast.show();
                return true;
            }

            Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
            startActivity(intent);
        }
        if (id == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;

        }
        if (drawerToggle.onOptionsItemSelected(item)) {
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

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);

    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        switch (menuItem.getItemId()) {
            case R.id.nav_choose_store:
                break;
            case R.id.nav_old_orders:
                Intent intent = new Intent(MainActivity.this, PreviousOrdersActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
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
        databaseUtils.getShoppingListByListId(this, listId);
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
            shoppingList.addItems(item);
            updateCart();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationUtils.getCurrentPlace(this);
                } else {
                    Log.d(TAG, "permission denied");
                }
                return;
            }
        }
    }


    @Override
    public void OnLocationFetchListener(ArrayList<String> locations) {
        for (String location : locations) {
            Toast.makeText(this, location, Toast.LENGTH_SHORT).show();
            databaseUtils.setUserLocation(FacebookUtils.getFacebookId(), Profile.getCurrentProfile().getFirstName(), location);
        }
    }

    @Override
    public void onStoreSelection(String storeName) {
        MDToast mdToast = MDToast.makeText(getApplicationContext(), "Your pick up store " + storeName + " is saved." , MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
        mdToast.show();
        shoppingList.setStore(storeName);
    }

    private void initializeShoppingList() {
        shoppingList.setListId(CommonUtils.getUuid());
    }

    private void initializeCart() {
        tvCartCount = (TextView) findViewById(R.id.tvCartCount);
        tvCartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(shoppingList==null ){
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Your cart is empty !", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                    return;
                }
                if(shoppingList.getStore()==null){
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Please choose a store", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                    return;
                }
                if(shoppingList.getItems()==null){
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Your cart is empty, please add atleast one item", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(intent);
            }
        });
        ivCart = (ImageView) findViewById(R.id.ivCart);
        ivCart.setVisibility(View.VISIBLE);
    }

    private void updateCart() {
        if (shoppingList.getItems() != null && shoppingList.getItems().size() > 0) {
            tvCartCount.setVisibility(View.VISIBLE);
            tvCartCount.setText(Integer.toString(shoppingList.getItems().size()));
        }
    }

    @Override
    public void OnAddItem(ShoppableItem shoppableItem) {
        shoppingList.addItems(shoppableItem);
        updateCart();
    }
}

