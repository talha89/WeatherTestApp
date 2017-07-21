package com.mooncascade.weathertestapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.Utility;
import com.mooncascade.weathertestapp.Words;
import com.mooncascade.weathertestapp.adapters.CitiesWeatherRecyclerViewAdapter;
import com.mooncascade.weathertestapp.common.ClearableAutoCompleteTextView;
import com.mooncascade.weathertestapp.data.model.BaseModel;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;
import com.mooncascade.weathertestapp.rest.RestClient;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MainFragment extends BaseFragment {

    RecyclerView recyclerView;
    CitiesWeatherRecyclerViewAdapter adapter;
    Map countriesBoundingMap;
    ClearableAutoCompleteTextView autoCompleteTextView;
    boolean isShowAsTextEnabled = false;
    private View mProgressView;

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
        recyclerView = view.findViewById(R.id.list);

        mProgressView = view.findViewById(R.id.login_progress);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new CitiesWeatherRecyclerViewAdapter(new ArrayList<>(), mListener);
        recyclerView.setAdapter(adapter);

        countriesBoundingMap = Utility.readFileToHashMap(context);

        //TODO: move autocompleteview setting to another function
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);

        Set<String> keys = countriesBoundingMap.keySet();
        String[] array = keys.toArray(new String[keys.size()]);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, array);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String query = countriesBoundingMap.get(autoCompleteTextView.getText().toString()) + ",10";
                Log.i("query", query);
                dispatchCall(query);
                hideKeyboard(view);
            }
        });

        dispatchCall("22,58,28,60,10");

    }

    void dispatchCall(String params) {
        showProgress(true, mProgressView, recyclerView);
        RestClient.getInstance(context).getCityWeathers(params);
    }

    @Subscribe
    public void onWeatherReceived(ArrayList<CityTempBaseModel> data) {
        showProgress(false, mProgressView, recyclerView);
        adapter.updateData(data);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onRestError(BaseModel error) {
        showProgress(false, mProgressView, recyclerView);
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
            recyclerView.invalidate();
            adapter.notifyDataSetChanged();
            isShowAsTextEnabled = Utility.isShowAsTextEnabled(context);
        }
    }

}
