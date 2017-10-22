package com.codepath.socialshopper.socialshopper.Utils;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        StringBuilder friendIds = new StringBuilder();
                        Log.d(TAG, response.toString());
                        try {

                            JSONArray jsonArray = response.getJSONObject().getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                friendIds.append(jsonArray.getJSONObject(i).getString("id")).append(",");
                            }
                            DatabaseUtils.setUserFriends(friendIds.toString());
                            Log.d(TAG, friendIds.toString());
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    public String getFBID(){

        return "";
    }

}
