package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.FacebookUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.shaishavgandhi.loginbuttons.FacebookButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    LoginButton loginButton;
    FacebookButton fbButton;
    CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;

    private static final String TAG = "SocShpLoginAct";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupBackgroundVideo();

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

    private void setFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback < LoginResult > () {
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
                        showHomeScreen();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                }
        );

        fbButton = (FacebookButton) findViewById(R.id.fbLogin);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, FacebookUtils.getPermissions());
            }
        });
    }

    private void showHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setupBackgroundVideo() {
        VideoView videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.background);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }


    // pass context to Calligraphy
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }
}
