package com.mooncascade.weathertestapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mooncascade.weathertestapp.data.model.BaseModel;
import com.mooncascade.weathertestapp.data.model.CityForecastBaseModel;
import com.mooncascade.weathertestapp.data.model.CityModel;
import com.mooncascade.weathertestapp.rest.RestClient;
import com.mooncascade.weathertestapp.views.ForecastView;
import com.mooncascade.weathertestapp.views.ForecastViewImpl;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends BaseFragment {

    public static final String LIST_ITEM = "listItem";
    public static final String LAT = "lat";
    public static final String LNG = "lng";

    private Long cityId;

    private double lat;
    private double lng;

    private ForecastView mViewMVC;
    ArrayList<CityForecastBaseModel> data;
    CityModel city;

    public static ForecastFragment newInstance(Long id) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putLong(LIST_ITEM, id);
        fragment.setArguments(args);
        return fragment;
    }

    public static ForecastFragment newInstance(double lat, double lng) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putDouble(LAT, lat);
        args.putDouble(LNG, lng);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            cityId = getArguments().getLong(LIST_ITEM);
            lat = getArguments().getDouble(LAT);
            lng = getArguments().getDouble(LNG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewMVC = new ForecastViewImpl(inflater, container);

        return mViewMVC.getRootView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            if (cityId != 0)
                dispatchCall(cityId);
            else
                dispatchCall(lat, lng);
        } else{
            mViewMVC.updateList(data);
            getActivity().setTitle(city.getName());
        }

    }

    void dispatchCall(Long id) {
        mViewMVC.showProgress(true);
        RestClient.getInstance(context).getCityForecast(id);
    }

    void dispatchCall(double lat, double lng) {
        mViewMVC.showProgress(true);
        RestClient.getInstance(context).getCityForecastByCoordinates(lat, lng);
    }

    @Subscribe
    public void onForecastReceived(ArrayList<CityForecastBaseModel> data) {
        this.data = data;
        mViewMVC.showProgress(false);
        mViewMVC.updateList(data);
    }

    @Subscribe
    public void onCityInfoReceived(CityModel city) {
        this.city = city;
        if (getActivity() != null) {
            getActivity().setTitle(city.getName());
        }
    }

    @Subscribe
    public void onRestError(BaseModel error) {
        mViewMVC.showProgress(false);
        displayToast(error.getErrorMessage());
    }

}
