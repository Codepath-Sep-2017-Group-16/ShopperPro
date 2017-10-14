package com.codepath.socialshopper.socialshopper.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.R;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        processIntentAction(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntentAction(intent);
        super.onNewIntent(intent);
    }

    private void processIntentAction(Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case "YES_ACTION":
                    Toast.makeText(this, "Yes :)", Toast.LENGTH_SHORT).show();
                    break;
                case "NO_ACTION":
                    Toast.makeText(this, "No :(", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
