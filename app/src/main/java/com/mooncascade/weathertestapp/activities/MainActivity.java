package com.mooncascade.weathertestapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;
import com.mooncascade.weathertestapp.fragments.ForecastFragment;
import com.mooncascade.weathertestapp.fragments.MainFragment;

public class MainActivity extends BaseActivity implements MainFragment.OnListFragmentInteractionListener {

    private static final String TAG_MAIN_FRAGMENT = "MainFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openMainFragment();

    }

    private void openMainFragment() {
        if (findFragmentByTag(TAG_MAIN_FRAGMENT) == null)
            replaceFragmentWithTag(new MainFragment(), R.id.container, TAG_MAIN_FRAGMENT);
    }

    @Override
    public void onListFragmentInteraction(CityTempBaseModel item) {

        Bundle bundle = new Bundle();
        bundle.putLong(ForecastFragment.LIST_ITEM, item.getId());
        launchActivityWithBundle(ForecastActivity.class, bundle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                launchActivity(SettingsActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
