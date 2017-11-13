package com.codepath.socialshopper.socialshopper.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.codepath.socialshopper.socialshopper.Utils.NotificationType;
import com.facebook.Profile;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import com.valdesekamdem.library.mdtoast.MDToast;

/**
 * Created by saripirala on 11/12/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat{

    private SwitchPreference mShopperSwitchPreference;
    private SwitchPreference mRequestorSwitchPreference;
    private DatabaseUtils dbUtils;


    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_notification, rootKey);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dbUtils = new DatabaseUtils();

        mShopperSwitchPreference = (SwitchPreference) getPreferenceManager().findPreference("shopperNotifKey");
        mRequestorSwitchPreference = (SwitchPreference) getPreferenceManager().findPreference("requestorNotifKey");


        mShopperSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (Boolean)newValue;
                dbUtils.setNotificationPreferences(Profile.getCurrentProfile().getId(), NotificationType.SHOPPER_NOTIFICATION_PREFERENCE, value);
                MDToast mdToast = MDToast.makeText(getContext(), "Your notification preference is saved", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                mdToast.show();
                return true;
            }
        });

        mRequestorSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (Boolean)newValue;
                dbUtils.setNotificationPreferences(Profile.getCurrentProfile().getId(), NotificationType.SHOPPER_NOTIFICATION_PREFERENCE, value);
                MDToast mdToast = MDToast.makeText(getContext(), "Your notification preference is saved", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                mdToast.show();
                return true;
            }
        });


        return super.onCreateView(inflater, container, savedInstanceState);
    }



}
