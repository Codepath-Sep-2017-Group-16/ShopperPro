package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Adapters.ShoppingListArrayAdapter;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.FacebookUtils;
import com.codepath.socialshopper.socialshopper.Utils.LocationUtils;
import com.codepath.socialshopper.socialshopper.Utils.Status;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;
import com.facebook.Profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.codepath.socialshopper.socialshopper.Activities.MainActivity.shoppingList;
import static com.codepath.socialshopper.socialshopper.R.id.toolbar_title;
import static com.codepath.socialshopper.socialshopper.Utils.FacebookUtils.getFacebookId;

public class ShoppingListActivity extends AppCompatActivity implements LocationUtils.OnLocationFetchListener {
    ShoppingListArrayAdapter adapter;
    RecyclerView rvShoppingListItems;
    SwipeButton swipeButton;
    private LocationUtils locationUtils;
    private DatabaseUtils databaseUtils;
    public final String TAG = "SocShpLstSub";
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    @BindView((R.id.toolbar))
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ButterKnife.bind(this);
        setUpToolBar();

        adapter = new ShoppingListArrayAdapter(shoppingList.getItems());

        rvShoppingListItems = (RecyclerView) findViewById(R.id.rvShoppingListItems);
        rvShoppingListItems.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getBaseContext());
        rvShoppingListItems.setLayoutManager(manager);
        rvShoppingListItems.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        swipeButton = (SwipeButton) findViewById(R.id.swipe_btn);

        swipeButton.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                submitShoppingList(swipeButton);
            }
        });
        setupTransitions();
        setupLocationService();
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final TextView toolbarTitle = (TextView) findViewById(toolbar_title);
        //ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) toolbarTitle.getLayoutParams();
        //layoutParams.setMarginStart(0);
        toolbarTitle.setText(shoppingList.getStore());
    }

    public void submitShoppingList(View view) {
        saveList(shoppingList);
        Intent intent = new Intent(ShoppingListActivity.this, TrackStatusActivity.class);

        intent.putExtra("list_id", shoppingList.getListId());
        intent.putExtra("store", shoppingList.getStore());

        DatabaseUtils.updateListStatus(shoppingList.getListId(), Status.PLACED);
        startActivity(intent);
        shoppingList.clear();
        finish();
    }

    private void saveList(ShoppingList shoppingList) {
        Log.i(TAG, "SavingList with these items: " + shoppingList.getStore() + " " + shoppingList.getListId());
        for (ShoppableItem item :
                shoppingList.getItems()) {
            Log.i(TAG, item.getmItemName());
            shoppingList.setUserId(FacebookUtils.getFacebookId());
            shoppingList.setStatus("CREATED");
            shoppingList.setCreatedTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.US).format(new Date()));
            DatabaseUtils.saveList(FacebookUtils.getFacebookId(), shoppingList);
        }
    }


    // pass context to Calligraphy
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    private void setupTransitions() {
        Transition slide =
                TransitionInflater.from(this).
                        inflateTransition(R.transition.transition_slide_right);
        getWindow().setEnterTransition(slide);
    }

    private void setupLocationService() {
        locationUtils = new LocationUtils();
        locationUtils.initializePlaces(this);

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
            databaseUtils.setUserLocation(getFacebookId(), Profile.getCurrentProfile().getFirstName(), location);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
