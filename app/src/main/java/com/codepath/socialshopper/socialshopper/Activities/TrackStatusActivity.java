package com.codepath.socialshopper.socialshopper.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class TrackStatusActivity extends AppCompatActivity
        implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, DatabaseUtils.OnLocationFetchListener,DatabaseUtils.OnImageFetchListenerInterface {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private static final String TAG = "SocShpTrkAct";
    private Marker currentLocationMarker;
    private DatabaseUtils dbUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_status);
        dbUtils = new DatabaseUtils();
        processIntentAction(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntentAction(intent);
        super.onNewIntent(intent);
    }

    private void processIntentAction(Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();
        Log.d(TAG, "status is " + intent.getStringExtra("status"));

        if(intent.getStringExtra("status")  != null) {
            String status = intent.getStringExtra("status");
            String listId = intent.getStringExtra("listId");
            Log.d(TAG, "status is " + status);
            TextView tvCheckBackStatus = (TextView) findViewById(R.id.tvCheckBackStatus);
            ImageView ivReceiptImg = (ImageView) findViewById(R.id.ivReceiptImg);
            Button btnRequestLocation = (Button) findViewById(R.id.btnRequestLocation);
            ivReceiptImg.setVisibility(View.GONE);
            //PICKED_UP, COMPLETED and OUT_FOR_DELIVERY
            if (status.equals("PICKED_UP")) {
                tvCheckBackStatus.setText(R.string.label_pickedup);
            }
            if (status.equals("COMPLETED")) {
                tvCheckBackStatus.setText(R.string.label_completed);
                //getimage; draw it into ivReceiptImg
                ivReceiptImg.setVisibility(View.VISIBLE);
                dbUtils.getImageForList(this,listId);
            }
            if (status.equals("OUT_FOR_DELIVERY")) {
                tvCheckBackStatus.setText(R.string.label_outfordel);
                //The map and the button can be hidden until this.
            }
        }
    }

    public void requestForShoppersLocation(View view) {

        // Send a notification to server saying that requestor would like to know shoppers location.
        // Server sends share location request, if the requestor

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {

                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
    }


    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
        //    Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();

//            String list = "0747f2ff-a631-42fa-a07c-b875d793e0a7";

            DatabaseUtils.getShoppersLocation(this, getIntent().getStringExtra("listId"));
            //DatabaseUtils.getShoppersLocation(this, list);
        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void OnLocationFetchListener(com.codepath.socialshopper.socialshopper.Models.Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if(currentLocationMarker !=null)
            currentLocationMarker.remove();

        currentLocationMarker = map.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Shopper")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(14).tilt(30).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void OnImageFetchListener(String imageURL) {
        if(!imageURL.isEmpty()) {
            Log.i(TAG,"Image is " + imageURL);
            ImageView ivReceiptImg = (ImageView) findViewById(R.id.ivReceiptImg);
            Glide.with(this.getBaseContext())
                    .load(imageURL)
                    .into(ivReceiptImg);
        }
    }
}
