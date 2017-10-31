package com.codepath.socialshopper.socialshopper.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Manifest;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Services.ShareLocationService;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.Status;
import com.skyfishjy.library.RippleBackground;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShareLocationActivity extends AppCompatActivity {

    public static final String RECEIVE_JSON = "RECEIVE_JSON";
    Double latitude, longitude;
    String provider;
    BroadcastReceiver receiver;
    Button btnLocationSharing;
    ImageView imageView;
    LocalBroadcastManager bManager;
    IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_location);

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.shareLocation);
        imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setTag(R.drawable.ic_location_on_black_24dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((Integer)imageView.getTag()==R.drawable.ic_location_on_black_24dp) {
                    ActivityCompat.requestPermissions(ShareLocationActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    imageView.setImageResource(R.drawable.ic_location_off_black_24dp);
                    imageView.setTag(R.drawable.ic_location_off_black_24dp);
                    rippleBackground.startRippleAnimation();
                    bManager.registerReceiver(receiver, intentFilter);
                } else if((Integer)imageView.getTag()==R.drawable.ic_location_off_black_24dp) {
                    imageView.setImageResource(R.drawable.ic_location_on_black_24dp);
                    imageView.setTag(R.drawable.ic_location_on_black_24dp);
                    rippleBackground.stopRippleAnimation();
                    rippleBackground.clearAnimation();
                    rippleBackground.setLayoutAnimationListener(null);
                    bManager.unregisterReceiver(receiver);
                }
            }
        });

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

        bManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
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
                    Intent intent = new Intent(ShareLocationActivity.this, ShareLocationService.class);
                    startService(intent);
//                    btnLocationSharing.setText("Stop location sharing");
                    imageView.setImageResource(R.drawable.ic_location_off_black_24dp);
                    imageView.setTag(R.drawable.ic_location_off_black_24dp);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    // pass context to Calligraphy
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }
}
