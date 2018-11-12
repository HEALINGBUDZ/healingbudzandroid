
package com.codingpixel.healingbudz.network.BudzFeedModel.ReportModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportPostResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("successMessage")
    @Expose
    private String successMessage;
    @SerializedName("successData")
    @Expose
    private FlagPost successData;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

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

    public FlagPost getSuccessData() {
        return successData;
    }

    public void setSuccessData(FlagPost successData) {
        this.successData = successData;
    }

}
