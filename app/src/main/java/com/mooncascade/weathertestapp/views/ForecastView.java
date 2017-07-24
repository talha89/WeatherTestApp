package com.mooncascade.weathertestapp.views;

import com.mooncascade.weathertestapp.data.model.CityForecastBaseModel;

import java.util.ArrayList;

/**
 * Created by Talha Mir on 22-Jul-17.
 */
public interface ForecastView extends ViewMvc{

    void updateList(ArrayList<CityForecastBaseModel> data);

    void showProgress(boolean show);
}
