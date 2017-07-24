package com.mooncascade.weathertestapp.views;

import android.view.View;

import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;

import java.util.ArrayList;

/**
 * Created by Talha Mir on 22-Jul-17.
 */
public interface MainView extends ViewMvc  {

    interface MainViewInteractionListener {

        void onListItemClickListener(CityTempBaseModel item);

        void onCountrySelected(String boundingBoxString);
    }

    void updateList(ArrayList<CityTempBaseModel> data);

    void setListener(MainViewInteractionListener listener);

    void showProgress(boolean show);

    void refreshListView();
}
