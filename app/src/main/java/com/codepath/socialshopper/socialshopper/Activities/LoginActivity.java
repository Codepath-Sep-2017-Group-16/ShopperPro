package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.FacebookUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;

    private static final String TAG = "SocShpLoginAct";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.codepath.facebooklogin", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i(TAG, Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "Exception1" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.i(TAG, "Exception2" + e.getMessage());
        }
        catch (Exception e){
            Log.i(TAG, "Exception3" + e.getMessage());
        }
        if (FacebookUtils.isLoggedIn()) {
            showHomeScreen();
            return;
        }
        setFacebookLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setFacebookLogin(){
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(FacebookUtils.getPermissions());

        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            Log.i("facebook - profile", currentProfile.getFirstName());
                            FacebookUtils.fbID = currentProfile.getId();
                            DatabaseUtils.saveGCMRegistrationIDAndUserInfo(FacebookUtils.getFacebookId(), currentProfile.getFirstName());
                            FacebookUtils.getFacebookFriendsMembers();
                        }
                    };

                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    FacebookUtils.fbID = profile.getId();
                    Log.i(TAG+"fb-prof", profile.getFirstName());
                }
                //DatabaseUtils.saveGCMRegistrationIDAndUserInfo(FacebookUtils.getFacebookId(), Profile.getCurrentProfile().getFirstName());
                //FacebookUtils.getFacebookFriendsMembers();
                showHomeScreen();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "login cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i(TAG, "login error");
            }
        });
    }
    private void showHomeScreen() {
        Intent intent = new Intent(this, ChooseStoreActivity.class);
        startActivity(intent);
    }
}
