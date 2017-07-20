package com.mooncascade.weathertestapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.Utility;
import com.mooncascade.weathertestapp.Words;
import com.mooncascade.weathertestapp.data.model.CityTempBaseModel;
import com.mooncascade.weathertestapp.data.model.WeatherModel;
import com.mooncascade.weathertestapp.fragments.MainFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CitiesWeatherRecyclerViewAdapter extends RecyclerView.Adapter<CitiesWeatherRecyclerViewAdapter.ViewHolder> {

    private ArrayList<CityTempBaseModel> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CitiesWeatherRecyclerViewAdapter(ArrayList<CityTempBaseModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void updateData(ArrayList<CityTempBaseModel> data) {
        mValues = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Context cxt = holder.cityWeatherIv.getContext();

        holder.cityTitleTv.setText(mValues.get(position).getCityName());
        if(Utility.isShowAsTextEnabled(cxt)){
            holder.cityTempTv.setText(String.format(cxt.getString(R.string.weather_temp_as_text), Words.EnglishNumberToWords((int) mValues.get(position).getTemperature().getTemp())));
        }
        else{
            holder.cityTempTv.setText(String.format(cxt.getString(R.string.weather_temp), mValues.get(position).getTemperature().getTemp()));
        }

        ArrayList<WeatherModel> weatherModels = mValues.get(position).getWeather();
        if(weatherModels != null && weatherModels.size() > 0){
            holder.cityWeatherTv.setText(weatherModels.get(0).getMainWeather());

            String url = String.format(cxt.getString(R.string.weather_icon_url),
                    mValues.get(position).getWeather().get(0).getIcon() + ".png");

            Picasso.with(cxt).load(url).into(holder.cityWeatherIv);

        }


        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView cityTitleTv;
        public final TextView cityTempTv;
        public final TextView cityWeatherTv;
        public final ImageView cityWeatherIv;

        public CityTempBaseModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            cityTitleTv = view.findViewById(R.id.cityTitleTv);
            cityTempTv = view.findViewById(R.id.cityTempTv);
            cityWeatherTv = view.findViewById(R.id.cityWeatherTv);
            cityWeatherIv = view.findViewById(R.id.cityWeatherIv);
        }

/*        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }*/
    }
}
