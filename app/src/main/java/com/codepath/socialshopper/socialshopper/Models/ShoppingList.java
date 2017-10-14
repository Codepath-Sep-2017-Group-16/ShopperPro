package com.codepath.socialshopper.socialshopper.Models;

import java.util.ArrayList;

/**
 * Created by rdeshpan on 10/14/2017.
 */

public class ShoppingList {
    String listId;
    ArrayList<ShoppableItem> items;

    public ShoppingList(String listId, ArrayList<ShoppableItem> items) {
        this.listId = listId;
        this.items = items;
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
        return items;
    }

    public void setItems(ArrayList<ShoppableItem> items) {
        this.items = items;
    }
}
