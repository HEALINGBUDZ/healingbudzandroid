package com.codingpixel.healingbudz.DataModel;

/**
 * Created by incubasyss on 27/03/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reward {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("user_rewards_count")
    @Expose
    private Integer userRewardsCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Reward withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Reward withTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Reward withPoints(Integer points) {
        this.points = points;
        return this;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Reward withCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Reward withUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Integer getUserRewardsCount() {
        return userRewardsCount;
    }

    public void setUserRewardsCount(Integer userRewardsCount) {
        this.userRewardsCount = userRewardsCount;
    }

    public Reward withUserRewardsCount(Integer userRewardsCount) {
        this.userRewardsCount = userRewardsCount;
        return this;
    }

}