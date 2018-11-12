package com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class File implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("thumbnail")
    @Expose
    private String thumb;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("ratio")
    @Expose
    private String ratio;

    public String getRatio() {
        if (ratio == null || ratio.length() == 0) {
            return "1";
        }else if(ratio.equalsIgnoreCase("null")){
            return "1";
        }
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getFileType() {
        if (type == null) {
            return 0;
        }
        if (type.trim().toLowerCase().equals("image")) {
            return 0;
        }
        if (type.trim().toLowerCase().equals("video")) {
            return 1;
        }
        return 0;
    }
}