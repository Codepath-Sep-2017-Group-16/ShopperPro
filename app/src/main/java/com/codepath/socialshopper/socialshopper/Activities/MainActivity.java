package com.codepath.socialshopper.socialshopper.Activities;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.socialshopper.socialshopper.Adapters.ShoppableItemsArrayAdapter;
import com.codepath.socialshopper.socialshopper.Fragments.AddItemDetailsDialogFragment;
import com.codepath.socialshopper.socialshopper.Fragments.DairyFragment;
import com.codepath.socialshopper.socialshopper.Fragments.FruitsFragment;
import com.codepath.socialshopper.socialshopper.Fragments.MeatFragment;
import com.codepath.socialshopper.socialshopper.Fragments.StoresFragment;
import com.codepath.socialshopper.socialshopper.Fragments.VegetableFragment;
import com.codepath.socialshopper.socialshopper.Listeners.AppBarStateChangeListener;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.CommonUtils;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.FacebookUtils;
import com.codepath.socialshopper.socialshopper.Utils.LocationUtils;
import com.crashlytics.android.Crashlytics;
import com.facebook.login.LoginManager;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.goodiebag.carouselpicker.CarouselPicker;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.codepath.socialshopper.socialshopper.R.id.toolbar_title;
import static com.codepath.socialshopper.socialshopper.Utils.FacebookUtils.getFacebookId;


public class MainActivity extends AppCompatActivity implements
        DatabaseUtils.OnActiveListsFetchListener, DatabaseUtils.OnListFetchListener, AddItemDetailsDialogFragment.AddItemDetailsDialogListener,
        StoresFragment.OnStoreFragmentInteractionListener, ShoppableItemsArrayAdapter.OnUpdateItemListener {

    public static ShoppingList shoppingList = new ShoppingList();
    public final String TAG = "SocShpMainAct";
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    @BindView(R.id.nvView)
    NavigationView nvDrawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView((R.id.toolbar))
    Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appBarLayout;
    @BindView(R.id.ivChosenStore) ImageView ivChosenStore;
    ActionBarDrawerToggle drawerToggle;
    TextView tvCartCount;
    ImageView ivCart;
    int shopPosition = 0;
    String[] stores = {"Costco Wholesale","Walmart", "Lucky" ,"Whole Foods Market", "Target", "Safeway"};
    int[] store_logos = {R.drawable.storelogo_costco, R.drawable.storelogo_walmart,R.drawable.storelogo_lucky, R.drawable.storelogo_wholefoods,R.drawable.storelogo_target,R.drawable.storelogo_safeway };
    private DatabaseUtils databaseUtils;
    private LocationUtils locationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        databaseUtils = new DatabaseUtils();

        setUpToolBar();
        setUpInitialScreen();

        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        initializeShoppingList();
        initializeCart();
        setupTransitions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCart();
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
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_dehaze_white_24dp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final TextView toolbarTitle = (TextView) findViewById(toolbar_title);
        toolbarTitle.setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) toolbarTitle.getLayoutParams();
        layoutParams.setMarginStart(0);

        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(toolbar, "Y", -1000, 0);
        moveAnim.setDuration(1300);
        moveAnim.setInterpolator(new DecelerateInterpolator());
        //moveAnim.setStartDelay(1000L);
        moveAnim.start();

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("SocShpSTATE", state.name());
                if(state.name().equals("COLLAPSED")) {
                    ivChosenStore.setVisibility(View.VISIBLE);
                    toolbarTitle.setVisibility(View.VISIBLE);
                    ivChosenStore.setImageResource(store_logos[shopPosition]);
                }
                if(state.name().equals("EXPANDED")) {
                    ivChosenStore.setVisibility(View.GONE);
                    toolbarTitle.setVisibility(View.GONE);
                }
            }
        });
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
//
//        FragmentTransaction ftStore = getSupportFragmentManager().beginTransaction();
//        ftStore.replace(R.id.storeFragment, new StoresFragment());
//        ftStore.commit();

        CarouselPicker carouselPicker = (CarouselPicker) findViewById(R.id.carousel);

        List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
        //{"Costco Wholesale","Walmart","Whole Foods Market", "Target", "Safeway", "Lucky"};
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.storelogo_costco)); //Costco Wholesale
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.storelogo_walmart)); //Walmart
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.storelogo_lucky)); //Lucky
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.storelogo_wholefoods)); // Whole Foods Market
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.storelogo_target)); //Target
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.storelogo_safeway)); //Safeway
        //Create an adapter
        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(this, imageItems, 0);
        //Set the adapter
        carouselPicker.setAdapter(imageAdapter);
        shoppingList.setStore(stores[0]);
        ivChosenStore.setImageResource(store_logos[0]);

        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //position of the selected item
                shopPosition = position;
                shoppingList.setStore(stores[position]);
                ivChosenStore.setImageResource(store_logos[shopPosition]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

            if (shoppingList == null) {
                MDToast mdToast = MDToast.makeText(this, "Your cart is empty !", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                mdToast.show();
                return true;
            }
            if (shoppingList.getStore() == null) {
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
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        ImageView ivHeaderPhoto = headerLayout.findViewById(R.id.ivHeader);
        String profImgURL = "https://graph.facebook.com/" + FacebookUtils.getFacebookId() + "/picture?type=large";
        Log.i(TAG, "image url " + profImgURL);
        Glide.with(this)
                .load(profImgURL)
                .apply(RequestOptions.circleCropTransform())
                .into(ivHeaderPhoto);
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
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.nav_logout:
                LoginManager.getInstance().logOut();
                Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(logoutIntent);
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
        databaseUtils.getActiveLists(this, getFacebookId());
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
            shoppingList.addItem(item);
            updateCart();
        }
    }




    @Override
    public void onStoreSelection(String storeName) {
        shoppingList.setStore(storeName);
        Snackbar.make(findViewById(R.id.drawer_layout), "Shopping at " + storeName, Snackbar.LENGTH_INDEFINITE)
                .setAction("Change Store", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shoppingList.clear();
                    }
                })
                .show();
    }

    private void initializeShoppingList() {
        shoppingList.setListId(CommonUtils.getUuid());
    }

    private void initializeCart() {
        tvCartCount = (TextView) findViewById(R.id.tvCartCount);
        tvCartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shoppingList == null) {
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Your cart is empty !", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                    return;
                }
                if (shoppingList.getStore() == null) {
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Please choose a store", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                    return;
                }
                if (shoppingList.getItems() == null) {
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Your cart is empty, please add atleast one item", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                //startActivity(intent);
            }
        });
        ivCart = (ImageView) findViewById(R.id.ivCart);
        tvCartCount.setVisibility(View.GONE);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoppingList == null) {
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Your cart is empty !", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                    return;
                }
                if (shoppingList.getStore() == null) {
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Please choose a store", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                    return;
                }
                if (shoppingList.getItems() == null) {
                    MDToast mdToast = MDToast.makeText(getApplicationContext(), "Your cart is empty, please add atleast one item", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                //startActivity(intent);
            }
        });
        ivCart.setVisibility(View.VISIBLE);
    }

    private void updateCart() {
        if (shoppingList.getItems() != null && shoppingList.getItems().size() > 0) {
            tvCartCount.setVisibility(View.VISIBLE);
            final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
            tvCartCount.startAnimation(animShake);
            tvCartCount.setText(Integer.toString(shoppingList.getItems().size()));
        }
    }

    @Override
    public void OnUpdateItem(ShoppableItem shoppableItem) {
        if (shoppableItem.getmItemQty() > 0)
            shoppingList.addItem(shoppableItem);
        else
            shoppingList.removeItem(shoppableItem);

        updateCart();
    }


    // pass context to Calligraphy
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    public void pickStore(View view) {
        shoppingList.setStore(stores[shopPosition]);
    }

    private void setupTransitions() {
        Transition exitSlide =
                TransitionInflater.from(this).
                        inflateTransition(R.transition.transition_slide_left);
        Transition enterSlide =
                TransitionInflater.from(this).
                        inflateTransition(R.transition.transition_slide_right);
        enterSlide.setDuration(500);
        getWindow().setExitTransition(exitSlide);
        getWindow().setEnterTransition(enterSlide);
    }
}

