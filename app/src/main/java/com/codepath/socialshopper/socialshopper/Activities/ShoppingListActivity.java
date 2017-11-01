package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codepath.socialshopper.socialshopper.Adapters.ShoppingListArrayAdapter;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.FacebookUtils;
import com.codepath.socialshopper.socialshopper.Utils.Status;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.codepath.socialshopper.socialshopper.Activities.MainActivity.shoppingList;

public class ShoppingListActivity extends AppCompatActivity {
    ShoppingListArrayAdapter adapter;
    RecyclerView rvShoppingListItems;
    SwipeButton swipeButton;
    public final String TAG = "SocShpLstSub";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

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
}
