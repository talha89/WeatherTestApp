package com.mooncascade.weathertestapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Talha Mir on 18-Jul-17.
 */
public class Utility {

    public static boolean isNetworkAvailable(Context cxt) {
        ConnectivityManager cm = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected();
    }

    public static Map readFileToHashMap(Context cxt) {
        Map<String, String> map;
        try {
            map = new HashMap<String, String>();
            Resources res = cxt.getResources();
            InputStream is = res.openRawResource(R.raw.country_boundingboxes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null) {
                // Log.i("line", line);
                String[] pieces = line.trim().split("\\s+");
                // Log.i("pieces", pieces.length + "");
                map.put(pieces[0], pieces[1] + "," +
                        pieces[2] + "," +
                        pieces[3] + "," +
                        pieces[4]);

                line = reader.readLine();
            }
            reader.close();
            is.close();
            Log.i("readFileToHashMap", String.valueOf(map.keySet().size()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }

    private static long RoundToWholeNumber(String number) {

        Double num = Double.parseDouble(number);
        return Math.round(num);
    }

    public static boolean isShowAsTextEnabled(Context cxt) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(cxt);
        return sharedPref.getBoolean(cxt.getString(R.string.setting_key1), false);
    }

}
