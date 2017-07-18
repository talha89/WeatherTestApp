package com.mooncascade.weathertestapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.data.model.CityForecastBaseModel;
import com.mooncascade.weathertestapp.data.model.WeatherModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by Talha Mir on 18-Jul-17.
 */
public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CityForecastBaseModel> mValues;

    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_HEADER = 0;

    private final int mHeaderRes;

    private LinkedHashMap<Integer, String> sectionsIndexer;

    public ForecastRecyclerViewAdapter(ArrayList<CityForecastBaseModel> items, int headerLayout) {
        mValues = items;
        mHeaderRes = headerLayout;

        sectionsIndexer = new LinkedHashMap<Integer, String>();
        calculateSectionHeaders();
    }

    public void updateData(ArrayList<CityForecastBaseModel> data) {
        mValues = data;
        calculateSectionHeaders();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(mHeaderRes, parent, false);
            return new SegmentViewHolder(itemView);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.forecast_list_item, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        if (viewType == TYPE_HEADER)
            ((SegmentViewHolder) holder).segmentName.setText(sectionsIndexer.get(position));
        else {
            ViewHolder sectionItemHolder = (ViewHolder) holder;
            CityForecastBaseModel thisItem = mValues.get(getSectionForPosition(position));

            sectionItemHolder.mItem = thisItem;

            String dateString = thisItem.getDateString();
            if (!TextUtils.isEmpty(dateString)) {
                sectionItemHolder.forecastTimeTv.setText(dateString.split(" ")[1]);
            }

            sectionItemHolder.cityTempTv.setText(String.format(Locale.getDefault(), "%.2f", thisItem.getTemperature().getTemp()));
            sectionItemHolder.foreCastWindTv.setText(String.format(Locale.getDefault(), "%.2f", thisItem.getWind().getSpeed()));

            ArrayList<WeatherModel> weatherModels = thisItem.getWeather();
            if (weatherModels != null && weatherModels.size() > 0) {
                // holder.cityWeatherTv.setText(weatherModels.get(0).getMainWeather());

                Context cxt = sectionItemHolder.cityWeatherIv.getContext();

                String url = String.format(cxt.getString(R.string.weather_icon_url),
                        thisItem.getWeather().get(0).getIcon() + ".png");

                Picasso.with(cxt).load(url).into(sectionItemHolder.cityWeatherIv);

            }
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size() + sectionsIndexer.keySet().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView forecastTimeTv;
        public final ImageView cityWeatherIv;
        public final TextView cityTempTv;
        public final TextView foreCastWindTv;

        public CityForecastBaseModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            forecastTimeTv = view.findViewById(R.id.forecastTimeTv);
            cityTempTv = view.findViewById(R.id.cityTempTv);
            foreCastWindTv = view.findViewById(R.id.foreCastWindTv);
            cityWeatherIv = view.findViewById(R.id.cityWeatherIv);
        }
    }

    public static class SegmentViewHolder extends RecyclerView.ViewHolder {
        public TextView segmentName;

        public SegmentViewHolder(View view) {
            super(view);
            segmentName = view.findViewById(R.id.segmentNameTV);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getPositionForSection(position)) {
            return TYPE_NORMAL;
        }
        return TYPE_HEADER;
    }

    public int getPositionForSection(int section) {
        if (sectionsIndexer.containsKey(section)) {
            return section + 1;
        }
        return section;
    }

    public int getSectionForPosition(int position) {
        int offset = 0;
        for (Integer key : sectionsIndexer.keySet()) {
            if (position > key) {
                offset++;
            } else {
                break;
            }
        }

        return position - offset;
    }

    private void calculateSectionHeaders() {
        String previous = "";
        int count = 0;

        sectionsIndexer.clear();

        for (int i = 0; i < mValues.size(); i++) {

            String group = "";

            String dateString = mValues.get(i).getDateString();
            if (!TextUtils.isEmpty(dateString)) {
                group = dateString.split(" ")[0];
            }

            if (!previous.equals(group)) {
                sectionsIndexer.put(i + count, group);
                previous = group;

                Log.v("Section Creation", "Group " + group + "at position: " + (i + count));

                count++;
            }
        }
    }

}
