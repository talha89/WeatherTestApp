package com.mooncascade.weathertestapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public class TemperatureModel {

    @SerializedName("temp")
    int temp;

    @SerializedName("temp_min")
    int tempMin;

    @SerializedName("temp_max")
    int tempMax;

    @SerializedName("pressure")
    int pressure;

    @SerializedName("humidity")
    int humidity;

}
