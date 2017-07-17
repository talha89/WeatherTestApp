package com.mooncascade.weathertestapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public class CityTempBaseModel {

    @SerializedName("id")
    long id;

    @SerializedName("name")
    String cityName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    @SerializedName("coord")
    CoordinatesModel coordinates;

    @SerializedName("main")
    TemperatureModel temperature;

    @SerializedName("wind")
    WindModel wind;

    @SerializedName("weather")
    ArrayList<WeatherModel> weather;

    public CoordinatesModel getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesModel coordinates) {
        this.coordinates = coordinates;
    }

    public TemperatureModel getTemperature() {
        return temperature;
    }

    public void setTemperature(TemperatureModel temperature) {
        this.temperature = temperature;
    }

    public WindModel getWind() {
        return wind;
    }

    public void setWind(WindModel wind) {
        this.wind = wind;
    }

    public ArrayList<WeatherModel> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherModel> weather) {
        this.weather = weather;
    }
}
