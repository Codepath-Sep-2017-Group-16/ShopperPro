package com.codepath.socialshopper.socialshopper.Models;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

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
    HashMap<String, Integer> itemMap;

    public String getListId() {
        return listId;
    }

    public ShoppingList() {
        itemMap = new HashMap<>();
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

    public void addItem(ShoppableItem item) {
        if(shoppableItems == null) {
            this.shoppableItems = new ArrayList<>();
        }
        if (itemMap.containsKey(item.getmItemName())) {
            int position = itemMap.get(item.getmItemName());
            shoppableItems.set(position, item);
        } else {
            shoppableItems.add(item);
            int position = shoppableItems.size()-1;
            itemMap.put(item.getmItemName(), position);
        }
    }

    public void removeItem(ShoppableItem item) {
        if (itemMap.containsKey(item.getmItemName())) {
            int position = itemMap.get(item.getmItemName());
            shoppableItems.remove(position);
            itemMap.remove(item.getmItemName());
        }
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
