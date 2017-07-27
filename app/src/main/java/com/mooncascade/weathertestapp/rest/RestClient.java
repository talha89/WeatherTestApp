package com.mooncascade.weathertestapp.rest;

import android.content.Context;

import com.mooncascade.weathertestapp.bus.BusProvider;
import com.mooncascade.weathertestapp.data.model.BaseJsonModel;
import com.mooncascade.weathertestapp.data.model.BaseModel;
import com.mooncascade.weathertestapp.data.model.CityForecastBaseModel;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
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

    public static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static RestClient ourInstance = new RestClient();
    private static ApiService apiService;
    private static String appId = "7131489711d7591432be8f2172cf6ca3";

    private RestClient() {
    }

    public static RestClient getInstance(Context cxt) {

        if (apiService == null) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //  if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
            //   }

            // This can be problematic in case of server redirects
            builder.addInterceptor(chain -> {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("APPID", appId).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            });

            builder.addInterceptor(new OfflineResponseCacheInterceptor())
                    // Set the cache location and size (10 MB)
                    .cache(new Cache(new File(cxt.getExternalCacheDir(), "http-cache"), 10 * 1024 * 1024));

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

    private Callback<BaseJsonModel<List<CityForecastBaseModel>>> forecastCallback = new Callback<BaseJsonModel<List<CityForecastBaseModel>>>() {
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
    };

    public void getCityForecast(long cityId) {

        Call<BaseJsonModel<List<CityForecastBaseModel>>> call = getApiService().getCityForecast(cityId, "metric");

        call.enqueue(forecastCallback);
    }

    public void getCityForecastByCoordinates(double lat, double lng) {

        Call<BaseJsonModel<List<CityForecastBaseModel>>> call = getApiService().getCityForecastByCoordinates(lat, lng, "metric");

        call.enqueue(forecastCallback);
    }

    private void dispatchFailureMessage(String string) {

        BaseModel mError = new BaseModel(200, string);

        dispatchFailureMessage(mError);
    }

    private void dispatchFailureMessage(BaseModel mError) {

        BusProvider.getInstance().post(mError);
    }

    private static class OfflineResponseCacheInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            try {
                return chain.proceed(chain.request());
            } catch (Exception e) {
                Request offlineRequest = chain.request().newBuilder()
                        .header("Cache-Control", "public, only-if-cached," +
                                "max-stale=" + 60 * 60 * 24)
                        .build();
                return chain.proceed(offlineRequest);
            }
        }
    }

}
