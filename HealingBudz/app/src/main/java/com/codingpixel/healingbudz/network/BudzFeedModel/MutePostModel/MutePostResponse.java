
package com.codingpixel.healingbudz.network.BudzFeedModel.MutePostModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MutePostResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("successMessage")
    @Expose
    private String successMessage;
    @SerializedName("successData")
    @Expose
    private MutePost successData;
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

    public MutePost getSuccessData() {
        return successData;
    }

    public void setSuccessData(MutePost successData) {
        this.successData = successData;
    }

}
