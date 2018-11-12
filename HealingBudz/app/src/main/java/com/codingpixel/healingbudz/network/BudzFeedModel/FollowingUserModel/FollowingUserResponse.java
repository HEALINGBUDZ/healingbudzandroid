
package com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowingUserResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("successMessage")
    @Expose
    private String successMessage;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("successData")
    @Expose
    private List<FollowingUser> successData = null;

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

    public List<FollowingUser> getSuccessData() {
        return successData;
    }

    public void setSuccessData(List<FollowingUser> successData) {
        this.successData = successData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
