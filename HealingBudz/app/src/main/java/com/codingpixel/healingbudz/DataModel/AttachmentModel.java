package com.codingpixel.healingbudz.DataModel;

/**
 * Created by macmini on 20/12/2017.
 */

public class AttachmentModel {

    public Integer id;
    public Integer strainId;
    public Integer userId;
    public Integer strainReviewId;
    public String attachment;
    public String type;
    public Object poster;
    public String createdAt;
    public String updatedAt;

    public AttachmentModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStrainId() {
        return strainId;
    }

    public void setStrainId(Integer strainId) {
        this.strainId = strainId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStrainReviewId() {
        return strainReviewId;
    }

    public void setStrainReviewId(Integer strainReviewId) {
        this.strainReviewId = strainReviewId;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getPoster() {
        return poster;
    }

    public void setPoster(Object poster) {
        this.poster = poster;
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
