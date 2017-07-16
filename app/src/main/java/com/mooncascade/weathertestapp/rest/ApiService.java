package com.mooncascade.weathertestapp.rest;

import com.mooncascade.weathertestapp.data.model.BaseJsonModel;
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

}
