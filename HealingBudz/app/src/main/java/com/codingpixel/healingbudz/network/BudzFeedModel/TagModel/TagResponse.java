package com.codingpixel.healingbudz.network.BudzFeedModel.TagModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TagResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("successMessage")
    @Expose
    private String successMessage;
    @SerializedName("successData")
    @Expose
    private List<Tag> successData = null;
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

    public List<Tag> getSuccessData() {
        return successData;
    }

    public void setSuccessData(List<Tag> successData) {
        this.successData = successData;
    }

}
