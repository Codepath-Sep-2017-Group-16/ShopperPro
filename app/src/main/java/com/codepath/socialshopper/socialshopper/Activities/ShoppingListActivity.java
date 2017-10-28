package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Adapters.ShoppingListArrayAdapter;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.FacebookUtils;
import com.codepath.socialshopper.socialshopper.Utils.Status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.codepath.socialshopper.socialshopper.Activities.MainActivity.shoppingList;

public class ShoppingListActivity extends AppCompatActivity {
    ShoppingListArrayAdapter adapter;
    RecyclerView rvShoppingListItems;
    public final String TAG = "SocShpLstSub";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        //TODO If shoppingList is empty just display an activity doing so.

        adapter = new ShoppingListArrayAdapter(shoppingList.getItems());

        rvShoppingListItems = (RecyclerView) findViewById(R.id.rvShoppingListItems);
        rvShoppingListItems.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getBaseContext());
        rvShoppingListItems.setLayoutManager(manager);
        Log.i(TAG, "shopping list activity");
        Log.i(TAG, "number of items in the list" + String.valueOf(shoppingList.getItems().size()));

    }

    public void submitShoppingList(View view) {
        Log.i(TAG, "submitting list");
        saveList(shoppingList);
        Toast.makeText(view.getContext(), "Published the shopping list", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ShoppingListActivity.this, TrackStatusActivity.class);
        intent.putExtra("list_id", shoppingList.getListId());
        DatabaseUtils.updateListStatus(shoppingList.getListId(), Status.PLACED);
        startActivity(intent);
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
}
