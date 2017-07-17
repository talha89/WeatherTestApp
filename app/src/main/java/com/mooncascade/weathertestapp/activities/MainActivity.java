package com.mooncascade.weathertestapp.activities;

import android.os.Bundle;
import android.util.Log;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;
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
        if(findFragmentByTag(TAG_MAIN_FRAGMENT) == null)
            replaceFragmentWithTag(new MainFragment(), R.id.container,TAG_MAIN_FRAGMENT);
    }

    @Override
    public void onListFragmentInteraction(CityTempBaseModel item) {

        Log.i("item clicked", item.getCityName());
    }

}
