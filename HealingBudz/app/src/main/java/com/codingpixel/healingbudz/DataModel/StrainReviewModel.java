package com.codingpixel.healingbudz.DataModel;

/**
 * Created by macmini on 20/12/2017.
 */

public class StrainReviewModel {


    public Integer id;
    public Integer strainId;
    public Integer reviewedBy;
    public String review;
    public String createdAt;
    public String updatedAt;
    public StrainModel getStrain;
    public double rating;
    public AttachmentModel attachment;
    String attatchment_type;
    String attatchment_poster;
    String attatchment_path;
    int user_id;
    String user_first_name;
    String user_image_path;
    String user_avatar;

    public StrainReviewModel() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_image_path() {
        return user_image_path;
    }

    public void setUser_image_path(String user_image_path) {
        this.user_image_path = user_image_path;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getAttatchment_type() {
        return attatchment_type;
    }

    public void setAttatchment_type(String attatchment_type) {
        this.attatchment_type = attatchment_type;
    }

    public String getAttatchment_poster() {
        return attatchment_poster;
    }

    public void setAttatchment_poster(String attatchment_poster) {
        this.attatchment_poster = attatchment_poster;
    }

    public String getAttatchment_path() {
        return attatchment_path;
    }

    public void setAttatchment_path(String attatchment_path) {
        this.attatchment_path = attatchment_path;
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

    public Integer getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
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

    public StrainModel getGetStrain() {
        return getStrain;
    }

    public void setGetStrain(StrainModel getStrain) {
        this.getStrain = getStrain;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public AttachmentModel getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentModel attachment) {
        this.attachment = attachment;
    }
}
