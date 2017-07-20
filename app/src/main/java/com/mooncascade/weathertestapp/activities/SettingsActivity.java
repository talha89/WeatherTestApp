package com.mooncascade.weathertestapp.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.fragments.SettingsFragment;

public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        replaceFragment(new SettingsFragment(), R.id.container);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.setting_key1))) {
            this.finish();
            launchActivity(MainActivity.class);
        }
    }

}
