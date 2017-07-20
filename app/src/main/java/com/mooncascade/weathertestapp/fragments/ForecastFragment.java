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
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends BaseFragment {

    public static final String LIST_ITEM = "listItem";

    private Long cityId;

    RecyclerView recyclerView;
    ForecastRecyclerViewAdapter adapter;

    private View mProgressView;

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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_forecast;
    }

    @Override
    public void initViews(View view) {
        recyclerView = view.findViewById(R.id.list);

        mProgressView = view.findViewById(R.id.login_progress);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ForecastRecyclerViewAdapter(new ArrayList<>(), R.layout.forecast_section_header);
        recyclerView.setAdapter(adapter);

        dispatchCall(cityId);
    }

    void dispatchCall(Long id) {
        showProgress(true, mProgressView, recyclerView);
        RestClient.getInstance(context).getCityForecast(id);
    }

    @Subscribe
    public void onForecastReceived(ArrayList<CityForecastBaseModel> data) {
        showProgress(false, mProgressView, recyclerView);
        adapter.updateData(data);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onCityInfoReceived(CityModel city) {
        if(getActivity()!=null){
            getActivity().setTitle(city.getName());
        }
    }

    @Subscribe
    public void onRestError(BaseModel error) {
        showProgress(false, mProgressView, recyclerView);
        displayToast(error.getErrorMessage());
    }

}
