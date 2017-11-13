package com.codepath.socialshopper.socialshopper.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.socialshopper.socialshopper.Fragments.FruitsFragment;
import com.codepath.socialshopper.socialshopper.Fragments.SettingsFragment;
import com.codepath.socialshopper.socialshopper.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
