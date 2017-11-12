package com.codepath.socialshopper.socialshopper.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.socialshopper.socialshopper.R;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by saripirala on 11/12/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat{

    private SwitchPreference mShopperSwitchPreference;
    private SwitchPreference mRequestorSwitchPreference;


    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_notification, rootKey);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mShopperSwitchPreference = (SwitchPreference) getPreferenceManager().findPreference("shopperNotifKey");
        mRequestorSwitchPreference = (SwitchPreference) getPreferenceManager().findPreference("requestorNotifKey");


        mShopperSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return false;
            }
        });

        mRequestorSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return false;
            }
        });

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
