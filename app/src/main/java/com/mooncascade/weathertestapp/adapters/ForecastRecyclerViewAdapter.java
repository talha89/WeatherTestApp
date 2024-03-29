package com.mooncascade.weathertestapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooncascade.weathertestapp.R;
import com.mooncascade.weathertestapp.common.Utility;
import com.mooncascade.weathertestapp.common.Words;
import com.mooncascade.weathertestapp.data.model.CityForecastBaseModel;
import com.mooncascade.weathertestapp.data.model.WeatherModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        sectionsIndexer = new LinkedHashMap<>();
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

            Context cxt = sectionItemHolder.cityWeatherIv.getContext();

            sectionItemHolder.mItem = thisItem;

            String dateString = thisItem.getDateString();
            if (!TextUtils.isEmpty(dateString)) {
                sectionItemHolder.forecastTimeTv.setText(getTime(dateString));
            }

            if (Utility.isShowAsTextEnabled(cxt)) {
                sectionItemHolder.cityTempTv.setText(String.format(cxt.getString(R.string.forecast_temp_as_text), Words.EnglishNumberToWords((int) thisItem.getTemperature().getTemp())));
            } else {
                sectionItemHolder.cityTempTv.setText(String.format(cxt.getString(R.string.weather_temp), thisItem.getTemperature().getTemp()));
            }

            //sectionItemHolder.cityTempTv.setText(String.format(cxt.getString(R.string.weather_temp), thisItem.getTemperature().getTemp()));
            sectionItemHolder.foreCastWindTv.setText(String.format(cxt.getString(R.string.weather_speed), thisItem.getWind().getSpeed()));

            ArrayList<WeatherModel> weatherModels = thisItem.getWeather();
            if (weatherModels != null && weatherModels.size() > 0) {
                // holder.cityWeatherTv.setText(weatherModels.get(0).getMainWeather());

                String url = String.format(cxt.getString(R.string.weather_icon_url),
                        weatherModels.get(0).getIcon() + ".png");

                Picasso.with(cxt).load(url).into(sectionItemHolder.cityWeatherIv);

                sectionItemHolder.foreCastWeatherTv.setText(weatherModels.get(0).getDescription());
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
        public final TextView foreCastWeatherTv;

        public CityForecastBaseModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            forecastTimeTv = view.findViewById(R.id.forecastTimeTv);
            cityTempTv = view.findViewById(R.id.cityTempTv);
            foreCastWindTv = view.findViewById(R.id.foreCastWindTv);
            cityWeatherIv = view.findViewById(R.id.cityWeatherIv);
            foreCastWeatherTv = view.findViewById(R.id.foreCastWeatherTv);
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

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date date = format.parse(group);

                    if (isToday(date))
                        group = "Today";
                    else if (isTomorrow(date))
                        group = "Tomorrow";

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            if (!previous.equals(group)) {
                sectionsIndexer.put(i + count, group);
                previous = group;

                Log.v("Section Creation", "Group " + group + "at position: " + (i + count));

                count++;
            }
        }
    }

    private String getTime(String timeString) {

        String displayValue = "";

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormatter.parse(timeString);
            // Get time from date
            SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
            displayValue = timeFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return displayValue;
    }

    public static boolean isToday(Date d) {
        return DateUtils.isToday(d.getTime());
    }

    public static boolean isTomorrow(Date d) {
        return DateUtils.isToday(d.getTime() - DateUtils.DAY_IN_MILLIS);
    }

}
