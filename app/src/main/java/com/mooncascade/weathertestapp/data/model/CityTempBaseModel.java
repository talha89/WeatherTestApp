package com.mooncascade.weathertestapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public class CityTempBaseModel {

    @SerializedName("id")
    long id;

    @SerializedName("dt")
    long dt;

    @SerializedName("name")
    String cityName;

    @SerializedName("coord")
    CoordinatesModel coordinates;

    @SerializedName("main")
    TemperatureModel temperature;

    @SerializedName("wind")
    WindModel wind;

}
