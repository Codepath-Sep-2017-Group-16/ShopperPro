package com.codepath.socialshopper.socialshopper.Utils;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.ProfilePictureView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rdeshpan on 10/12/2017.
 */

public class FacebookUtils {
    private final static String TAG = "FacebookUtils";
    private ProfileTracker mProfileTracker;
    public static String fbID;

    public static List<String> getPermissions() {
        return Arrays.asList("user_status", "email", "user_friends", "read_custom_friendlists", "public_profile");
    }

    public static boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired();
    }

    public static void logout() {
        // TODO : Implement this
    }

    public static String getFacebookId() {
        return Profile.getCurrentProfile().getId();
    }

    public static String getFacebookName() {
        return Profile.getCurrentProfile().getName();
    }

    public static void getFacebookFriendsMembers() {
        Log.d(TAG, "Calling Facebook Friends");
        Log.d(TAG, "Facebook ID: " +  getFacebookId());
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+ getFacebookId() +"/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, response.toString());
                    }
                }
        ).executeAsync();
    }

    public String getFBID(){

        return "";
    }

}
