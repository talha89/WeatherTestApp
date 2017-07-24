package com.mooncascade.weathertestapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.adapters.ForecastRecyclerViewAdapter;
import com.mooncascade.weathertestapp.data.model.BaseModel;
import com.mooncascade.weathertestapp.data.model.CityForecastBaseModel;
import com.mooncascade.weathertestapp.data.model.CityModel;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;
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

    private Long cityId;

    private ForecastView mViewMVC;
    ArrayList<CityForecastBaseModel> data;

    public static ForecastFragment newInstance(Long id) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putLong(LIST_ITEM, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            cityId = getArguments().getLong(LIST_ITEM);
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

        if (savedInstanceState == null)
            dispatchCall(cityId);
        else
            mViewMVC.updateList(data);
    }

    void dispatchCall(Long id) {
        mViewMVC.showProgress(true);
        RestClient.getInstance(context).getCityForecast(id);
    }

    @Subscribe
    public void onForecastReceived(ArrayList<CityForecastBaseModel> data) {
        this.data = data;
        mViewMVC.showProgress(false);
        mViewMVC.updateList(data);
    }

    @Subscribe
    public void onCityInfoReceived(CityModel city) {
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
