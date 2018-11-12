package com.codingpixel.healingbudz.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macmini on 06/04/2018.
 */

public class ReplyBudz implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("business_review_id")
    @Expose
    private Integer businessReviewId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("reply")
    @Expose
    private String reply;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusinessReviewId() {
        return businessReviewId;
    }

    public void setBusinessReviewId(Integer businessReviewId) {
        this.businessReviewId = businessReviewId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.businessReviewId);
        dest.writeValue(this.userId);
        dest.writeString(this.reply);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    public ReplyBudz() {
    }

    protected ReplyBudz(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.businessReviewId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.reply = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Parcelable.Creator<ReplyBudz> CREATOR = new Parcelable.Creator<ReplyBudz>() {
        @Override
        public ReplyBudz createFromParcel(Parcel source) {
            return new ReplyBudz(source);
        }

        @Override
        public ReplyBudz[] newArray(int size) {
            return new ReplyBudz[size];
        }
    };
}
