package com.mooncascade.weathertestapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Talha Mir on 18-Jul-17.
 */
public class CityModel {

    @SerializedName("id")
    Long id;

    @SerializedName("name")
    String name;

    @SerializedName("coord")
    CoordinatesModel coordinates;

    @SerializedName("country")
    String country;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoordinatesModel getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesModel coordinates) {
        this.coordinates = coordinates;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
