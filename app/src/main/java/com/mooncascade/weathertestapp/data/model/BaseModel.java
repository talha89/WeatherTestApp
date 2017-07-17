package com.mooncascade.weathertestapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public class BaseModel {

    public BaseModel() {
    }

    public BaseModel(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    @SerializedName("cod")
    private int status;

    @SerializedName("message")
    private String errorMessage;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
