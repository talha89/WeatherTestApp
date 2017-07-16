package com.mooncascade.weathertestapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public class WeatherModel {

    @SerializedName("id")
    long id;

    @SerializedName("main")
    String mainWeather;

    @SerializedName("dt")
    String description;

    @SerializedName("icon")
    String icon;

}
