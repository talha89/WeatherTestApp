package com.mooncascade.weathertestapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Talha Mir on 17-Jul-17.
 */
public class CityForecastBaseModel {

    @SerializedName("dt")
    long dt;

    @SerializedName("main")
    TemperatureModel temperature;

    @SerializedName("weather")
    ArrayList<WeatherModel> weather;

    @SerializedName("wind")
    WindModel wind;

    @SerializedName("dt_txt")
    String dateString;


    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public TemperatureModel getTemperature() {
        return temperature;
    }

    public void setTemperature(TemperatureModel temperature) {
        this.temperature = temperature;
    }

    public ArrayList<WeatherModel> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherModel> weather) {
        this.weather = weather;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public WindModel getWind() {
        return wind;
    }

    public void setWind(WindModel wind) {
        this.wind = wind;
    }
}
