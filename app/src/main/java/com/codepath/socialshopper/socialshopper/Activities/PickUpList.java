package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Adapters.ShoppersListArrayAdapter;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;

import java.util.ArrayList;

public class PickUpList extends AppCompatActivity implements DatabaseUtils.OnListFetchListener, DatabaseUtils.OnActiveListsFetchListener{

    private DatabaseUtils dbUtils;
    private ShoppersListArrayAdapter shoppersListArrayAdapter;
    private ArrayList<ShoppableItem> list;
    private RecyclerView rvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppers_list);
        list = new ArrayList<>();
        shoppersListArrayAdapter = new ShoppersListArrayAdapter(this, list);
        rvItems = (RecyclerView) findViewById(R.id.rvShoppingListItems);
        rvItems.setAdapter(shoppersListArrayAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        rvItems.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        dbUtils = new DatabaseUtils(this);
        processIntentAction(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        processIntentAction(intent);
        super.onNewIntent(intent);
    }

    private void processIntentAction(Intent intent) {

        String id = intent.getStringExtra("list_id");

        dbUtils.getShoppingListByListId(id);

//        if (intent.getAction() != null) {
//            switch (intent.getAction()) {
//                case "YES_ACTION":
//                    Toast.makeText(this, "Yes :)", Toast.LENGTH_SHORT).show();
//                    break;
//                case "NO_ACTION":
//                    Toast.makeText(this, "No :(", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
    }

    @Override
    public void OnListFetchListener(ShoppingList shoppingList) {
        list.addAll(shoppingList.getItems());
        updateAdapter();
    }

    @Override
    public void OnListsFetchListener(ArrayList<ShoppingList> shoppingLists) {
        //TODO
    }

    private void updateAdapter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shoppersListArrayAdapter.notifyDataSetChanged();
            }
        });
    }

}
