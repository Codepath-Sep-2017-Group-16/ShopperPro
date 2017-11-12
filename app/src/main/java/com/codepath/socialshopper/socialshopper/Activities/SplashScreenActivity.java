package com.codepath.socialshopper.socialshopper.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.Constants;
import com.codepath.socialshopper.socialshopper.Utils.FacebookUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.google.android.gms.wallet.Cart;
import com.stripe.wrap.pay.AndroidPayConfiguration;
import com.stripe.wrap.pay.activity.StripeAndroidPayActivity;
import com.stripe.wrap.pay.utils.CartContentException;
import com.stripe.wrap.pay.utils.CartManager;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashScreenActivity extends AwesomeSplash {

    // Implemented https://github.com/ViksaaSkool/AwesomeSplash

    @Override
    public void initSplash(ConfigSplash configSplash) {

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorGreen); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.splash); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash("");
        configSplash.setTitleTextColor(R.color.colorWhite);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(30);
        configSplash.setAnimTitleTechnique(Techniques.FadeIn);
        setupTransitions();

    }

    @Override
    public void animationsFinished() {
        Intent intent;

        if (!FacebookUtils.isLoggedIn()) {
            intent = new Intent(this, LoginActivity.class);
        }else{
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        //initializePayments();
        //finish();
    }

    private void setupTransitions() {
        Transition exitSlide =
                TransitionInflater.from(this).
                        inflateTransition(R.transition.transition_slide_left);

        //getWindow().setExitTransition(exitSlide);
    }

    private void initializePayments() {
        Intent intent;
        AndroidPayConfiguration payConfiguration = AndroidPayConfiguration.init(Constants.PUBLISHABLE_KEY, Constants.CURRENCY_CODE_USD);
        CartManager cartManager = new CartManager();
        cartManager.setTotalPrice(1L);
        try {
            Cart cart = cartManager.buildCart();
            intent = new Intent(this, PaymentActivity.class).putExtra(StripeAndroidPayActivity.EXTRA_CART, cart);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } catch (CartContentException e) {

            e.printStackTrace();
        }
    }
}
