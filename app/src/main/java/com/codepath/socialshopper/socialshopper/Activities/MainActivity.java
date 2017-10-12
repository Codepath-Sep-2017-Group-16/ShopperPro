package com.codepath.socialshopper.socialshopper.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.socialshopper.socialshopper.Fragments.HorizontalItemsFragment;
import com.codepath.socialshopper.socialshopper.R;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    public final String TAG = "SocShpMainAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        setUpInitialScreen();
    }

    private void setUpInitialScreen() {
        Log.i(TAG, "starting initial set-up");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flItems, new HorizontalItemsFragment());
        ft.commit();
    }
}
