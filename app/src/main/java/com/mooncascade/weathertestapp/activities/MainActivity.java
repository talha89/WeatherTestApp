package com.mooncascade.weathertestapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.rest.RestClient;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestClient.getInstance().getCityWeathers("22,58,28,60,10");

    }


}
