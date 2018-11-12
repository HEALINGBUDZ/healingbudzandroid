package com.codingpixel.healingbudz.network.BudzFeedModel.SubUsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * Created by M_Muzammil Sharif on 02-Apr-18.
 */
public class SubUsersResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("successMessage")
    @Expose
    private String successMessage;
    @SerializedName("successData")
    @Expose
    private SuccessData successData;
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

    public SuccessData getSuccessData() {
        return successData;
    }

    public void setSuccessData(SuccessData successData) {
        this.successData = successData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
