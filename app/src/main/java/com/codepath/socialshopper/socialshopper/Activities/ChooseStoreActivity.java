package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.codepath.socialshopper.socialshopper.Models.ShoppingList;
import com.codepath.socialshopper.socialshopper.R;

public class ChooseStoreActivity extends AppCompatActivity {
    static ShoppingList shoppingList = new ShoppingList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_store);
    }

    private void showMainScreen() {
        //Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(this, TrackStatusActivity.class);
        startActivity(intent);
    }

    public void pickCostco(View view) {
        shoppingList.setStore("Costco");
        showMainScreen();
    }

    public void pickWholeFoods(View view) {
        shoppingList.setStore("WholeFoods");
        showMainScreen();
    }

    public void pickTarget(View view) {
        shoppingList.setStore("Target");
        showMainScreen();
    }

    public void pickWalmart(View view) {
        shoppingList.setStore("Walmart");
        showMainScreen();
    }

    public void pickSafeway(View view) {
        shoppingList.setStore("Safeway");
        showMainScreen();
    }

    public void pickLuckys(View view) {
        shoppingList.setStore("Luckys");
        showMainScreen();
    }
}
