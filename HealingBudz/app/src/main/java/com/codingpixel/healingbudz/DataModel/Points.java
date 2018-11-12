package com.codingpixel.healingbudz.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by incubasyss on 27/03/2018.
 */

public class Points {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("points")
    @Expose
    private Integer points;
//    @SerializedName("type_id")
//    @Expose
//    private Integer typeId;
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

    public Points withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Points withType(String type) {
        this.type = type;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Points withUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Points withPoints(Integer points) {
        this.points = points;
        return this;
    }

//    public Integer getTypeId() {
//        return typeId;
//    }
//
//    public void setTypeId(Integer typeId) {
//        this.typeId = typeId;
//    }
//
//    public Points withTypeId(Integer typeId) {
//        this.typeId = typeId;
//        return this;
//    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Points withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Points withUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

}