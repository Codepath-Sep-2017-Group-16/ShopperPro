package com.codepath.socialshopper.socialshopper.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;

import java.util.ArrayList;

public class PreviousOrdersActivity extends AppCompatActivity implements DatabaseUtils.OnAllListFetchListener {
    private DatabaseUtils dbUtils;
    public final String TAG = "SocShpPrvOrd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_orders);

        dbUtils = new DatabaseUtils();
        dbUtils.getPastLists(this);
    }

    @Override
    public void OnAllListsFetchListener(ArrayList<ShoppingList> allShoppingLists) {
        try {
            for (ShoppingList list :
                    allShoppingLists) {
                Log.i(TAG, "List ID "+list.getListId());
                Log.i(TAG, "List store "+list.getStore());
            }
        }
        catch (Exception e){
            Log.i(TAG, "Exception" + e.getMessage());
        }

    }
}
