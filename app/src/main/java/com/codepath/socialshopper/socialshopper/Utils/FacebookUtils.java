package com.codepath.socialshopper.socialshopper.Utils;

import com.facebook.AccessToken;
import com.facebook.Profile;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rdeshpan on 10/12/2017.
 */

public class FacebookUtils {

    public static List<String> getPermissions() {
        return Arrays.asList("user_status", "email", "read_custom_friendlists");
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
}
