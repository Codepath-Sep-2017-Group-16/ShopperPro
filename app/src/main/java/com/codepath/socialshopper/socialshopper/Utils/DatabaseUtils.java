package com.codepath.socialshopper.socialshopper.Utils;

import android.app.Activity;
import android.util.Log;

import com.codepath.socialshopper.socialshopper.Models.Location;
import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.facebook.Profile;
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
    private static final String SHOPPER_LOCATION = "shopper_location";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String GCMID = "gcmid";
    private static final String DEVICE = "device";
    private static final String ACTIVE_LISTS = "active_lists";
    private static final String LOCATION = "location";
    private static final String CURRENT_LOCATION = "current_location";
    private static final String PAST_LISTS = "past_lists";
    private static final String ITEM = "item";
    private static final String TAG = "SocShpDbUtls";
    private static final String OPEN = "open";
    private static final String FIRST_NAME = "first_name";
    private static final String STORE_SAFEWAY = "Safeway";

    public DatabaseUtils() {

    }

    public void getActiveLists(Activity activity, String userId) {
        final OnActiveListsFetchListener mListenerLists = (OnActiveListsFetchListener) activity;
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

    public void getPastLists(Activity activity) {
        final OnAllListFetchListener mListenerListAll = (OnAllListFetchListener) activity;
        DatabaseReference ref = mDatabase.child(LISTS);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<ShoppingList> pastLists = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ShoppingList shoppingList = snapshot.getValue(ShoppingList.class);
                            Log.i(TAG,"Store " + shoppingList.getStore());
                            Log.i(TAG,"List #" + shoppingList.getItems().size());
//                            shoppingList.setListId(snapshot.getKey());
//                            shoppingList.setItems(snapshot.getValue;
                            pastLists.add(shoppingList);
                        }
                        mListenerListAll.OnAllListsFetchListener(pastLists);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    public void getShoppingListByListId(Activity activity, String listId) {
        final OnListFetchListener mListenerList = (OnListFetchListener) activity;
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
        mDatabase.child(STORES).child(STORE_SAFEWAY).child(shoppingList.getListId()).setValue(FacebookUtils.getFacebookId());

        mDatabase.child(SHOPPER_LOCATION).child(LATITUDE).child(shoppingList.getListId()).setValue(FacebookUtils.getFacebookId());
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

    public static void saveLocationOfShopper(String listId, Double latitude, Double longitude){
        mDatabase.child(LISTS).child(listId).child(CURRENT_LOCATION).child(LATITUDE).setValue(latitude);
        mDatabase.child(LISTS).child(listId).child(CURRENT_LOCATION).child(LONGITUDE).setValue(longitude);
    }

    public static void getShoppersLocation(Activity activity, final String listId) {
        final OnLocationFetchListener mListenerList = (OnLocationFetchListener) activity;
        DatabaseReference ref = mDatabase.child(LISTS).child(listId).child(CURRENT_LOCATION);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Location location = dataSnapshot.getValue(Location.class);
                mListenerList.OnLocationFetchListener(location);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public interface OnActiveListsFetchListener {
        void OnListsFetchListener(ArrayList<ShoppingList> shoppingLists);
    }

    public interface OnListFetchListener {
        void OnListFetchListener(ShoppingList shoppingList);
    }

    public interface OnLocationFetchListener {
        void OnLocationFetchListener(Location location);
    }

    public  interface OnAllListFetchListener {
        void OnAllListsFetchListener(ArrayList<ShoppingList> allShoppingLists);
    }
}
