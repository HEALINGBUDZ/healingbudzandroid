package com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * Created by M_Muzammil Sharif on 05-Apr-18.
 */
public class GetPostDetailResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("successMessage")
    @Expose
    private String successMessage;
    @SerializedName("successData")
    @Expose
    private PostDetailSuccessData successData;
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
        if (successMessage == null) {
            return "";
        }
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public PostDetailSuccessData getSuccessData() {
        return successData;
    }

    public void setSuccessData(PostDetailSuccessData successData) {
        this.successData = successData;
    }
}
