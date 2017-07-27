package com.mooncascade.weathertestapp.rest;

import com.mooncascade.weathertestapp.data.model.BaseJsonModel;
import com.mooncascade.weathertestapp.data.model.CityForecastBaseModel;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public interface ApiService {

    @GET("box/city")
    Call<BaseJsonModel<List<CityTempBaseModel>>> getCityWeathers(@Query("bbox") String start);

    @GET("forecast")
    Call<BaseJsonModel<List<CityForecastBaseModel>>> getCityForecast(@Query("id") long id, @Query("units") String units );

    @GET("forecast")
    Call<BaseJsonModel<List<CityForecastBaseModel>>> getCityForecastByCoordinates(@Query("lat") double lat, @Query("lon") double lng, @Query("units") String units );


}
