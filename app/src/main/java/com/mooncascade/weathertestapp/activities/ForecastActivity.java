package com.mooncascade.weathertestapp.activities;

import android.os.Bundle;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;
import com.mooncascade.weathertestapp.fragments.ForecastFragment;

/**
 * Created by Talha Mir on 17-Jul-17.
 */
public class ForecastActivity extends BaseActivity {

    private static final String TAG_FORECAST_FRAGMENT = "ForecastFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openForecastFragment();
    }

    private void openForecastFragment() {
        if (findFragmentByTag(TAG_FORECAST_FRAGMENT) == null) {
            Long id = getIntent().getLongExtra(ForecastFragment.LIST_ITEM,0);
            ForecastFragment forecastFragment = ForecastFragment.newInstance(id);
            replaceFragmentWithTag(forecastFragment, R.id.container, TAG_FORECAST_FRAGMENT);
        }

    }
}
