package com.codepath.socialshopper.socialshopper.Models;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rdeshpan on 10/14/2017.
 */

public class ShoppingList {
    String mListId;
    public final String TAG = "SocShpMainAct";
    ArrayList<ShoppableItem> mShoppableItems;

    public ShoppingList(String listId, ArrayList<ShoppableItem> shoppableItems) {
        this.mListId = listId;
        this.mShoppableItems = shoppableItems;
    }

    public String getListId() {
        return mListId;
    }

    public ShoppingList() {
    }

    public void setListId(String listId) {
        this.mListId = listId;
    }

    public ArrayList<ShoppableItem> getItems() {
        return mShoppableItems;
    }

    public void setItems(ArrayList<ShoppableItem> shoppableItems) {
        this.mShoppableItems = shoppableItems;
    }

    public void addItems(ShoppableItem item) {
        if(mShoppableItems == null) {
            this.mShoppableItems = new ArrayList<>();
        }
        this.mShoppableItems.add(item);
        Log.i(TAG, "Added " + item.getmItemName());
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "mListId='" + mListId + '\'' +
                ", TAG='" + TAG + '\'' +
                ", mShoppableItems=" + mShoppableItems +
                '}';
    }
}
