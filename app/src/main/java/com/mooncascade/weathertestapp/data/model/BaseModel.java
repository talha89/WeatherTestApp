package com.mooncascade.weathertestapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Talha Mir on 16-Jul-17.
 */
public class BaseModel {

    public BaseModel() {
    }

    public BaseModel(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    @SerializedName("cod")
    private boolean success;

    @SerializedName("message")
    private String errorMessage;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
