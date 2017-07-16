package com.mooncascade.weathertestapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public class CoordinatesModel {

    @SerializedName("Lat")
    double lat;

    @SerializedName("Lon")
    double lon;

}
