package com.mooncascade.weathertestapp.rest;

import android.support.compat.BuildConfig;

import com.mooncascade.weathertestapp.bus.BusProvider;
import com.mooncascade.weathertestapp.data.model.BaseJsonModel;
import com.mooncascade.weathertestapp.data.model.BaseModel;
import com.mooncascade.weathertestapp.data.model.CityForecastBaseModel;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public class RestClient {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static RestClient ourInstance = new RestClient();
    private static ApiService apiService;
    private static String appId = "7131489711d7591432be8f2172cf6ca3";

    private RestClient() {
    }

    public static RestClient getInstance() {

        if (apiService == null) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }

            builder.addNetworkInterceptor(chain -> {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("APPID", appId).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            });

            OkHttpClient client = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }

        return ourInstance;
    }

    public ApiService getApiService() {
        return apiService;
    }


    public void getCityWeathers(String coordinates) {

        Call<BaseJsonModel<List<CityTempBaseModel>>> call = getApiService().getCityWeathers(coordinates);

        call.enqueue(new Callback<BaseJsonModel<List<CityTempBaseModel>>>() {
            @Override
            public void onResponse(Call<BaseJsonModel<List<CityTempBaseModel>>> call, Response<BaseJsonModel<List<CityTempBaseModel>>> response) {

                if (response.isSuccessful()) {
                    BusProvider.getInstance().post(response.body().getData());

                } else {
                    try {
                        dispatchFailureMessage(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseJsonModel<List<CityTempBaseModel>>> call, Throwable t) {
                dispatchFailureMessage("Unexpected error...");
            }
        });
    }

    public void getCityForecast(long cityId) {

        Call<BaseJsonModel<List<CityForecastBaseModel>>> call = getApiService().getCityForecast(cityId, "metric");

        call.enqueue(new Callback<BaseJsonModel<List<CityForecastBaseModel>>>() {
            @Override
            public void onResponse(Call<BaseJsonModel<List<CityForecastBaseModel>>> call, Response<BaseJsonModel<List<CityForecastBaseModel>>> response) {
                if (response.isSuccessful()) {
                    BusProvider.getInstance().post(response.body().getData());
                    BusProvider.getInstance().post(response.body().getCity());
                } else {
                    try {
                        dispatchFailureMessage(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseJsonModel<List<CityForecastBaseModel>>> call, Throwable t) {
                dispatchFailureMessage("Unexpected error...");
            }
        });

    }

    private void dispatchFailureMessage(String string) {

        BaseModel mError = new BaseModel(200, string);

        dispatchFailureMessage(mError);
    }

    private void dispatchFailureMessage(BaseModel mError) {

        BusProvider.getInstance().post(mError);
    }


}
