package com.mooncascade.weathertestapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mooncascade.weathertestapp.common.Utility;
import com.mooncascade.weathertestapp.data.model.BaseModel;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;
import com.mooncascade.weathertestapp.rest.RestClient;
import com.mooncascade.weathertestapp.views.MainView;
import com.mooncascade.weathertestapp.views.MainViewImpl;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class MainFragment extends BaseFragment implements MainView.MainViewInteractionListener {

    boolean isShowAsTextEnabled = false;

    private MainView mViewMVC;

    ArrayList<CityTempBaseModel> data;

    @Override
    public void onListItemClickListener(CityTempBaseModel item) {
        Log.i("MainFragment","onListItemClickListener");
        mListener.onListFragmentInteraction(item);
    }

    @Override
    public void onCountrySelected(String boundingBoxString) {
        dispatchCall(boundingBoxString);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(CityTempBaseModel item);
    }

    private OnListFragmentInteractionListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewMVC = new MainViewImpl(inflater, container, this);

        return mViewMVC.getRootView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null)
            dispatchCall("22,58,28,60,10");
        else
            mViewMVC.updateList(data);
    }


    void dispatchCall(String params) {
        mViewMVC.showProgress(true);
        RestClient.getInstance(context).getCityWeathers(params);
    }

    @Subscribe
    public void onWeatherReceived(ArrayList<CityTempBaseModel> data) {
        this.data = data;
        mViewMVC.showProgress(false);
        mViewMVC.updateList(data);
    }

    @Subscribe
    public void onRestError(BaseModel error) {
        mViewMVC.showProgress(false);
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

    @Override
    public void onResume() {
        super.onResume();
        if (Utility.isShowAsTextEnabled(context) != isShowAsTextEnabled) {
            mViewMVC.refreshListView();
            isShowAsTextEnabled = Utility.isShowAsTextEnabled(context);
        }
    }

}
