package com.mooncascade.weathertestapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.adapters.CitiesWeatherRecyclerViewAdapter;
import com.mooncascade.weathertestapp.data.model.BaseModel;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;
import com.mooncascade.weathertestapp.rest.RestClient;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class MainFragment extends BaseFragment {

    RecyclerView recyclerView;
    CitiesWeatherRecyclerViewAdapter adapter;

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(CityTempBaseModel item);
    }

    private OnListFragmentInteractionListener mListener;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void initViews(View view) {
        recyclerView = (RecyclerView) view;

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new CitiesWeatherRecyclerViewAdapter(new ArrayList<>(), mListener);
        recyclerView.setAdapter(adapter);

        RestClient.getInstance().getCityWeathers("22,58,28,60,10");
    }

    @Subscribe
    public void onWeatherReceived(ArrayList<CityTempBaseModel> data) {
        adapter.updateData(data);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onRestError(BaseModel error) {
        displayToast(error.getErrorMessage());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
