
package com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SharedPost implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("sub_user_id")
    @Expose
    private Object subUserId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("json_data")
    @Expose
    private Object jsonData;
    @SerializedName("allow_repost")
    @Expose
    private Integer allowRepost;
    @SerializedName("shared_id")
    @Expose
    private Object sharedId;
    @SerializedName("shared_user_id")
    @Expose
    private Object sharedUserId;
    @SerializedName("original_name")
    @Expose
    private Object originalName;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(Object subUserId) {
        this.subUserId = subUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getJsonData() {
        return jsonData;
    }

    public void setJsonData(Object jsonData) {
        this.jsonData = jsonData;
    }

    public Integer getAllowRepost() {
        return allowRepost;
    }

    public void setAllowRepost(Integer allowRepost) {
        this.allowRepost = allowRepost;
    }

    public Object getSharedId() {
        return sharedId;
    }

    public void setSharedId(Object sharedId) {
        this.sharedId = sharedId;
    }

    public Object getSharedUserId() {
        return sharedUserId;
    }

    public void setSharedUserId(Object sharedUserId) {
        this.sharedUserId = sharedUserId;
    }

    public Object getOriginalName() {
        return originalName;
    }

    public void setOriginalName(Object originalName) {
        this.originalName = originalName;
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

}
