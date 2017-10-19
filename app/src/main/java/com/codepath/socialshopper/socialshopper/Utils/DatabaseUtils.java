package com.codepath.socialshopper.socialshopper.Utils;

import android.app.Activity;

import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

/**
 * Created by rdeshpan on 10/13/2017.
 */

public class DatabaseUtils {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private static final String USERS = "users";
    private static final String LISTS = "lists";
    private static final String STORES = "stores";
    private static final String GCMID = "gcmid";
    private static final String DEVICE = "device";
    private static final String ACTIVE_LISTS = "active_lists";
    private static final String LOCATION = "location";
    private static final String PAST_LISTS = "past_lists";
    private static final String ITEM = "item";
    private static final String TAG = "DatabaseUtils";
    private static final String OPEN = "open";
    private static final String FIRST_NAME = "first_name";
    private OnActiveListsFetchListener mListenerLists;
    private OnListFetchListener mListenerList;
    private static final String STORE_SAFEWAY = "Safeway";

    public DatabaseUtils(Activity activity) {
        mListenerLists = (OnActiveListsFetchListener) activity;
        mListenerList = (OnListFetchListener) activity;
    }

    public void getActiveLists(Activity activity, String userId) {

        mDatabase.child(USERS).child(userId).child(ACTIVE_LISTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ShoppingList> activeLists = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    ShoppingList shoppingList = new ShoppingList();
                    shoppingList.setListId(snapshot.getKey());
                    activeLists.add(shoppingList);
                }

                mListenerLists.OnListsFetchListener(activeLists);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static ArrayList<ShoppingList> getPastLists() {
        ArrayList<ShoppingList> pastLists = new ArrayList<>();

        // TODO : implement this

        return pastLists;
    }

    public void getShoppingListByListId(String listId) {
        mDatabase.child(LISTS).child(listId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                mListenerList.OnListFetchListener(shoppingList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void setUserLocation(String userId, String firstName, String location) {
        saveGCMRegistrationIDAndUserInfo(userId, firstName);
        mDatabase.child(USERS).child(userId).child(LOCATION).setValue(location);
    }

    public static void saveList(String userId, ShoppingList shoppingList) {
        // Save list instance
        mDatabase.child(LISTS).child(shoppingList.getListId()).setValue(shoppingList);

        // Attach list to user active lists
        mDatabase.child(USERS).child(userId).child(ACTIVE_LISTS).child(shoppingList.getListId()).setValue(OPEN);

        // Attach list to store
        mDatabase.child(STORES).child(STORE_SAFEWAY).child(shoppingList.getListId()).setValue(OPEN);
    }

    public static void deleteList() {
        // TODO : implement this
    }

    public static void acceptList() {
        // TODO : implement this
    }

    public static void saveGCMRegistrationIDAndUserInfo(String userId, String profileName) {
        mDatabase.child(USERS).child(userId).child(GCMID).setValue(FirebaseInstanceId.getInstance().getToken());
        mDatabase.child(USERS).child(userId).child(FIRST_NAME).setValue(profileName);
    }

    public interface OnActiveListsFetchListener {
        void OnListsFetchListener(ArrayList<ShoppingList> shoppingLists);
    }

    public interface OnListFetchListener {
        void OnListFetchListener(ShoppingList shoppingList);
    }
}
