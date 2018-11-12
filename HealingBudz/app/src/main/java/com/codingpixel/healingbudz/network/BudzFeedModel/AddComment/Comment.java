
package com.codingpixel.healingbudz.network.BudzFeedModel.AddComment;

import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User;
import com.codingpixel.healingbudz.network.BudzFeedModel.MentionTagJsonModel;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.Like;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Comment implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("likes_count")
    @Expose
    private Integer likes_count;
    @SerializedName("liked_count")
    @Expose
    private Integer liked_count;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("json_data")
    @Expose
    private String jsonData;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("attachment")
    @Expose
    private Attachment attachment;
    //likes
    @SerializedName("likes")
    @Expose
    private List<Like> likes = new ArrayList<>();

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

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getComment() {
        if (comment == null) {
            return "";
        }
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public MentionTagJsonModel[] getJson_Data() {
        MentionTagJsonModel[] d = new Gson().fromJson(jsonData, MentionTagJsonModel[].class);
        if (d == null) {
            return new MentionTagJsonModel[]{};
        }
        return d;
    }

    public Integer getLikesCount() {
        return likes_count;
    }

    public void setLikesCount(Integer likes_count) {
        this.likes_count = likes_count;
    }

    public Integer getLiked_count() {
        return liked_count;
    }

    public void setLiked_count(Integer liked_count) {
        this.liked_count = liked_count;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
}
