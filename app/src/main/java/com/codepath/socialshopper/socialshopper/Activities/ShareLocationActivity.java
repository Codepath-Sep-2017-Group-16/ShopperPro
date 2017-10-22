package com.codepath.socialshopper.socialshopper.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Manifest;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Receivers.LocationService;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.Status;

public class ShareLocationActivity extends AppCompatActivity {

    public static final String RECEIVE_JSON = "RECEIVE_JSON";
    Double latitude, longitude;
    String provider;
    BroadcastReceiver receiver;

    Button btnLocationSharing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_location);
        btnLocationSharing = (Button) findViewById(R.id.btnShareLocation);
        initListener();


        receiver  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(RECEIVE_JSON)) {
                    provider = intent.getStringExtra("provider");
                    latitude = (Double)intent.getExtras().get("latitude");
                    longitude = (Double)intent.getExtras().get("longitude");



                    if(latitude!=null) {
                        Log.i("Coordinates", "Lat:" + latitude + " ,Long:" + longitude);
                        DatabaseUtils.saveLocationOfShopper(getIntent().getStringExtra("list_id"), latitude, longitude);
                    }

                }
            }
        };

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_JSON);
        bManager.registerReceiver(receiver, intentFilter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(ShareLocationActivity.this, LocationService.class);
                    startService(intent);
                    btnLocationSharing.setText("Stop location sharing");
                    DatabaseUtils.updateListStatus(getIntent().getStringExtra("list_id"), Status.OUT_FOR_DELIVERY);

                } else {
                    Toast.makeText(getApplicationContext(),"Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void initListener() {
        btnLocationSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnLocationSharing.getText().toString().equalsIgnoreCase("Start location sharing")){
                    ActivityCompat.requestPermissions(ShareLocationActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }else{
                    btnLocationSharing.setText("Start location sharing");
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        });
    }
}
