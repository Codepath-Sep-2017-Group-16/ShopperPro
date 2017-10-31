package com.codepath.socialshopper.socialshopper.Services;

import android.content.SharedPreferences;
import android.text.format.DateUtils;
import android.util.Log;

import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by saripirala on 10/11/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    private static final String TAG = "MyFbInstanceIDService";
    public static String REGISTRATION_ID;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        REGISTRATION_ID = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token: " + refreshedToken);
    }

}
