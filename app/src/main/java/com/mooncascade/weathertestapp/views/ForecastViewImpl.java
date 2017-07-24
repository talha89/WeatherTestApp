package com.mooncascade.weathertestapp.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.adapters.ForecastRecyclerViewAdapter;
import com.mooncascade.weathertestapp.data.model.CityForecastBaseModel;

import java.util.ArrayList;

/**
 * Created by Talha Mir on 22-Jul-17.
 */
public class ForecastViewImpl implements ForecastView {

    private View mRootView;

    RecyclerView recyclerView;
    ForecastRecyclerViewAdapter adapter;

    View mProgressView;

    public ForecastViewImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        initialize();
    }

    private void initialize() {
        recyclerView = mRootView.findViewById(R.id.list);

        mProgressView = mRootView.findViewById(R.id.login_progress);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mRootView.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ForecastRecyclerViewAdapter(new ArrayList<>(), R.layout.forecast_section_header);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void updateList(ArrayList<CityForecastBaseModel> data) {
        adapter.updateData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress(boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = recyclerView.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);

            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            recyclerView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

}
