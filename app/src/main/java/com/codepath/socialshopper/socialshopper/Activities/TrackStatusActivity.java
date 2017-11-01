package com.codepath.socialshopper.socialshopper.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.Adapters.TimeLineAdapter;
import com.codepath.socialshopper.socialshopper.Models.Location;
import com.codepath.socialshopper.socialshopper.Models.TimeLineModel;
import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.DateTimeUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TrackStatusActivity extends AppCompatActivity implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, DatabaseUtils.OnLocationFetchListener,DatabaseUtils.OnImageFetchListenerInterface {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private static final String TAG = "TrackStatusActivity";
    private Marker currentLocationMarker;
    private DatabaseUtils dbUtils;
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private static String status;
    private static String storeName;
    private Location prevLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_status);

        dbUtils = new DatabaseUtils();
        processIntentAction(getIntent());
        status = "SUBMITTED";
        initView();
        instantiateMapFragment();
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
            String message = intent.getStringExtra("message");

            String shopperName = message.split(" ")[0];

            Log.d(TAG, "status is " + status);
            //TextView tvCheckBackStatus = (TextView) findViewById(R.id.tvCheckBackStatus);
            //ImageView ivReceiptImg = (ImageView) findViewById(R.id.ivReceiptImg);
            //Button btnRequestLocation = (Button) findViewById(R.id.btnRequestLocation);
           // ivReceiptImg.setVisibility(View.GONE);
            //PICKED_UP, COMPLETED and OUT_FOR_DELIVERY
            if (status.equals("PICKED_UP")) {
                mDataList.add(new TimeLineModel(shopperName + " is around " + storeName + " and is shopping for you!", DateTimeUtils.getCurrentDateTime(), "PICKED_UP"));
                mTimeLineAdapter.notifyDataSetChanged();
            }
            if (status.equals("COMPLETED")) {
                mDataList.add(new TimeLineModel(shopperName + " has completed shopping for you. \uD83D\uDD7A", DateTimeUtils.getCurrentDateTime(), "COMPLETED"));
                mTimeLineAdapter.notifyDataSetChanged();
                //dbUtils.getImageForList(this,listId);
            }
        }
    }


    @Override
    public void OnLocationFetchListener(Location location) {


        if(location ==null || location.getLatitude()==null || location.getLongitude()==null)
            return; //No point in loading something which is null

        if(!mapFragment.isVisible()){
            mapFragment.getView().setVisibility(View.VISIBLE);
        }

        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if(currentLocationMarker !=null)
            currentLocationMarker.remove();

        currentLocationMarker = map.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Shopper")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(14).tilt(30).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if(prevLocation !=null) {
            map.addPolyline(new PolylineOptions()
                    .add(new LatLng(prevLocation.getLatitude(), prevLocation.getLongitude()) , new LatLng(location.getLatitude(), location.getLongitude()))
                    .width(5)
                    .color(Color.GREEN));
        }else
            prevLocation = location;
    }


    @Override
    public void OnImageFetchListener(String imageURL) {
        if(!imageURL.isEmpty()) {
            Log.i(TAG,"Image is " + imageURL);
//            ImageView ivReceiptImg = (ImageView) findViewById(R.id.ivReceiptImg);
//            Glide.with(this.getBaseContext())
//                    .load(imageURL)
//                    .into(ivReceiptImg);
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

    private void instantiateMapFragment(){
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

        mapFragment.getView().setVisibility(View.INVISIBLE);
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {

            String listId = getIntent().getStringExtra("listId");
            if(listId ==null)
                listId = getIntent().getStringExtra("list_id");

            DatabaseUtils.getShoppersLocation(this, listId);
        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }


    private void initView() {
        if(storeName ==null){
            storeName = getIntent().getStringExtra("store");
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mTimeLineAdapter = new TimeLineAdapter(mDataList, true);
        mRecyclerView.setAdapter(mTimeLineAdapter);
        setDataListItems();
    }

    private void setDataListItems(){

        if(mDataList!=null&&mDataList.size()==0) {
            mDataList.add(new TimeLineModel("Awesome! We are letting your friends know about your shopping list! ☺", DateTimeUtils.getCurrentDateTime(), status));
            mTimeLineAdapter.notifyDataSetChanged();
        }
    }

    // pass context to Calligraphy
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }
}
