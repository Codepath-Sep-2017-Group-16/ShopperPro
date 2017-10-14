package com.codepath.socialshopper.socialshopper.Utils;

import android.app.Activity;
import android.util.Log;

import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by rdeshpan on 10/13/2017.
 */

public class DatabaseUtils {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private static final String USERS = "users";
    private static final String LISTS = "lists";
    private static final String ACTIVE_LISTS = "active_lists";
    private static final String PAST_LISTS = "past_lists";
    private static final String ITEM = "item";
    private static final String TAG = "DatabaseUtils";
    private static final String OPEN = "open";
    private OnActiveListsFetchListener mListenerLists;
    private OnListFetchListener mListenerList;

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

    public static void saveList(String userId, ShoppingList shoppingList) {
        // Save list instance
        mDatabase.child(LISTS).child(shoppingList.getListId()).setValue(shoppingList);

        // Attach list to user active lists
        mDatabase.child(USERS).child(userId).child(ACTIVE_LISTS).child(shoppingList.getListId()).setValue(OPEN);
    }

    public static void deleteList() {
        // TODO : implement this
    }

    public static void acceptList() {
        // TODO : implement this
    }

    public static void saveGCMRegistrationID(){

    }

    public interface OnActiveListsFetchListener {
        void OnListsFetchListener(ArrayList<ShoppingList> shoppingLists);
    }

    public interface OnListFetchListener {
        void OnListFetchListener(ShoppingList shoppingList);
    }
}
