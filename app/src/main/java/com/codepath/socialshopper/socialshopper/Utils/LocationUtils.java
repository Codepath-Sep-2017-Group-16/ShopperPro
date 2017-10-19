package com.codepath.socialshopper.socialshopper.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rdeshpan on 10/17/2017.
 */

public class LocationUtils {

    public static final String TAG = "LocationUtils";
    private PlaceDetectionClient mPlaceDetectionClient;
    private final int M_MAX_ENTRIES = 5;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private OnLocationFetchListener mListenerLocation;
    private final String STORE_SAFEWAY = "Safeway";
    private final String STORE_COSTCO = "Costco Wholesale";


    public void initializePlaces(Activity activity) {
        mListenerLocation =  (OnLocationFetchListener) activity;
        mPlaceDetectionClient = Places.getPlaceDetectionClient(activity, null);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            return;
        }

        getCurrentPlace(activity);
    }

    public void getCurrentPlace(final Activity activity) {
        mListenerLocation =  (OnLocationFetchListener) activity;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            ArrayList<String> locations = new ArrayList<String>();
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    String place = placeLikelihood.getPlace().getName().toString();
                                    Log.d(TAG, place);
                                    if (place.equals(STORE_SAFEWAY))
                                        locations.add(place);
                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }

                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }

                            mListenerLocation.OnLocationFetchListener(locations);
                        }
                    });
        }

    }

    public interface OnLocationFetchListener {
        void OnLocationFetchListener(ArrayList<String> locations);
    }
}
