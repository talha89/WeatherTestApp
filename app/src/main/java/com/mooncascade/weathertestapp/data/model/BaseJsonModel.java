package com.mooncascade.weathertestapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public class BaseJsonModel<T> extends BaseModel{

    @SerializedName("list")
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
