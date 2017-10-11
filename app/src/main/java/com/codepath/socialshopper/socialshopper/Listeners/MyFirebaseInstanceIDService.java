package com.codepath.socialshopper.socialshopper.Listeners;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by saripirala on 10/11/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    private static final String TAG = "MyFbInstanceIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        //String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d(TAG, "Token: " + refreshedToken);

        //sendRegistrationToServer(refreshedToken);
    }


    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

}
