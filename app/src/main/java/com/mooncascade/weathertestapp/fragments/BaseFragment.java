package com.mooncascade.weathertestapp.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.mooncascade.weathertestapp.bus.BusProvider;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public abstract class BaseFragment extends Fragment {

    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    protected void displayToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}

