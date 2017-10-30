package com.codepath.socialshopper.socialshopper;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by gumapathi on 10/29/17.
 */

public class SocialShopperApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Abel-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
