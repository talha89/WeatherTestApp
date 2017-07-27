package com.mooncascade.weathertestapp.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.adapters.CitiesWeatherRecyclerViewAdapter;
import com.mooncascade.weathertestapp.common.ClearableAutoCompleteTextView;
import com.mooncascade.weathertestapp.common.Utility;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Talha Mir on 22-Jul-17.
 */
public class MainViewImpl implements MainView {

    private View mRootView;
    MainViewInteractionListener mListener;

    RecyclerView recyclerView;
    CitiesWeatherRecyclerViewAdapter adapter;
    Map countriesBoundingMap;
    ClearableAutoCompleteTextView autoCompleteTextView;
    private View mProgressView;

    public MainViewImpl(LayoutInflater inflater, ViewGroup container, MainViewInteractionListener listener) {
        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListener = listener;
        initialize();
    }

    private void initialize() {
        recyclerView = mRootView.findViewById(R.id.list);

        mProgressView = mRootView.findViewById(R.id.login_progress);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mRootView.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new CitiesWeatherRecyclerViewAdapter(new ArrayList<>(), mListener);
        recyclerView.setAdapter(adapter);

        countriesBoundingMap = Utility.readFileToHashMap(mRootView.getContext());

        autoCompleteTextView = mRootView.findViewById(R.id.autoCompleteTextView);

        Set<String> keys = countriesBoundingMap.keySet();
        String[] array = keys.toArray(new String[keys.size()]);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(mRootView.getContext(), android.R.layout.simple_list_item_1, array);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener((adapterView, view1, i, l) -> {
            String query = countriesBoundingMap.get(autoCompleteTextView.getText().toString()) + ",10";
            Log.i("query", query);
            mListener.onCountrySelected(query);
            hideKeyboard(view1);
        });

    }

    @Override
    public void updateList(ArrayList<CityTempBaseModel> data) {
        adapter.updateData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setListener(MainViewInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void showProgress(boolean show) {

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

    }

    @Override
    public void refreshListView() {
        recyclerView.invalidate();
        adapter.notifyDataSetChanged();
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

}
