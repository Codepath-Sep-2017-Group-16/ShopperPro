package com.codepath.socialshopper.socialshopper.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ExpandableListView;

import com.codepath.socialshopper.socialshopper.Adapters.ExpandableListAdapter;
import com.codepath.socialshopper.socialshopper.Models.ShoppableItem;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreviousOrdersActivity extends AppCompatActivity implements DatabaseUtils.OnAllListFetchListener {
    public final String TAG = "SocShpPrvOrd";
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<ShoppableItem>> listDataChild;
    private DatabaseUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_orders);

        dbUtils = new DatabaseUtils();
        dbUtils.getPastLists(this);
    }


    private void prepareListData(ArrayList<ShoppingList> allShoppingLists) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ShoppableItem>>();

        int i = 0;
        for (ShoppingList list :
                allShoppingLists) {
            String store = list.getStore() == null ? "-" : list.getStore();
            listDataHeader.add("List from " + store);
            List<ShoppableItem> items = list.getItems();

            listDataChild.put(listDataHeader.get(i), items); // Header, Child data
            i++;
        }
    }

    @Override
    public void OnAllListsFetchListener(ArrayList<ShoppingList> allShoppingLists) {
        try {
            for (ShoppingList list :
                    allShoppingLists) {
                Log.i(TAG, "List ID " + list.getListId());
                Log.i(TAG, "List store " + list.getStore());
                for (ShoppableItem item :
                        list.getItems()) {
                    Log.i(TAG, "List items " + item.getmItemName());
                }
            }

            // get the listview
            expListView = (ExpandableListView) findViewById(R.id.lvExp);

            // preparing list data
            prepareListData(allShoppingLists);

            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

            // setting list adapter
            expListView.setAdapter(listAdapter);
            Log.i(TAG, "List number " + String.valueOf(allShoppingLists.size()));
        } catch (Exception e) {
            Log.i(TAG, "Exception " + e.getMessage());
        }

    }
}