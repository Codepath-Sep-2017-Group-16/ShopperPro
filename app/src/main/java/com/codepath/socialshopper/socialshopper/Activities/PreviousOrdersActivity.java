package com.codepath.socialshopper.socialshopper.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.Map;

public class PreviousOrdersActivity extends AppCompatActivity implements DatabaseUtils.OnAllListFetchListener {
    private DatabaseUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_orders);

        dbUtils = new DatabaseUtils(this);
        dbUtils.getPastLists();
    }

    @Override
    public void OnAllListFetchListener(Map<String, Object> allShoppingLists) {
        ArrayList<ShoppingList> shoppingListArrayList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : allShoppingLists.entrySet()){
            Map singleList = (Map) entry.getValue();
            shoppingListArrayList.add((ShoppingList) singleList);
        }

    }
}
