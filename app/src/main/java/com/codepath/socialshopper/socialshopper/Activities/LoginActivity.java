package com.codepath.socialshopper.socialshopper.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.FacebookUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackManager;

    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                showHomeScreen();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "login cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "login error");
            }
        });
    }
    private void showHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
