package com.codepath.socialshopper.socialshopper.Models;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rdeshpan on 10/14/2017.
 */

public class ShoppingList {
    String listId;
    private final String TAG = "SocShpShpLt";
    ArrayList<ShoppableItem> shoppableItems;
    String store;
    String userId;
    String status;
    String receiptImageURL;
    String createdTimeStamp;


    public ShoppingList(String listId, ArrayList<ShoppableItem> shoppableItems) {
        this.listId = listId;
        this.shoppableItems = shoppableItems;
    }

    public String getListId() {
        return listId;
    }

    public ShoppingList() {
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public ArrayList<ShoppableItem> getItems() {
        return shoppableItems;
    }

    public void setItems(ArrayList<ShoppableItem> shoppableItems) {
        this.shoppableItems = shoppableItems;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiptImageURL() {
        return receiptImageURL;
    }

    public void setReceiptImageURL(String receiptImageURL) {
        this.receiptImageURL = receiptImageURL;
    }

    public String getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(String createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public void addItems(ShoppableItem item) {
        if(shoppableItems == null) {
            this.shoppableItems = new ArrayList<>();
        }
        this.shoppableItems.add(item);
        Log.i(TAG, "Added " + item.getmItemName());
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "mListId='" + listId + '\'' +
                ", TAG='" + TAG + '\'' +
                ", mShoppableItems=" + shoppableItems +
                '}';
    }
}
