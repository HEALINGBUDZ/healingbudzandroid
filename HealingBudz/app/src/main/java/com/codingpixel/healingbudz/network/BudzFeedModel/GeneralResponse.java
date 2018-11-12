package com.codingpixel.healingbudz.network.BudzFeedModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * Created by M_Muzammil Sharif on 30-Mar-18.
 */
public class GeneralResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("successMessage")
    @Expose
    private String successMessage;
    @SerializedName("successData")
    @Expose
    private Object successData;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public Object getSuccessData() {
        return successData;
    }

    public void setSuccessData(Object successData) {
        this.successData = successData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
