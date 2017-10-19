package com.codepath.socialshopper.socialshopper.Receivers;

import android.app.Activity;

/**
 * Created by saripirala on 10/19/17.
 */

public class ShopperUpdates {

    private OnLocationChange mLocationUpdateListener;



    public ShopperUpdates(Activity activity){
        mLocationUpdateListener = (OnLocationChange) activity;
    }

        public interface OnLocationChange {
        void onLocationChange(double latitude, double longitude);
    }

    public void newLocation()
    {
                        if (System.currentTimeMillis()/10==0){
                            mLocationUpdateListener.onLocationChange(53.558, 9.927);
                        }else
                            mLocationUpdateListener.onLocationChange(53.551, 9.993);

    }
}
