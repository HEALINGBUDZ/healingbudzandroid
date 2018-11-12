package com.codingpixel.healingbudz.network.BudzFeedModel.AddComment;

/**
 * Created by macmini on 13/04/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllComment {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("successMessage")
    @Expose
    private String successMessage;
    @SerializedName("successData")
    @Expose
    private SuccessDataComment successData;

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

    public SuccessDataComment getSuccessData() {
        return successData;
    }

    public void setSuccessData(SuccessDataComment successData) {
        this.successData = successData;
    }

}