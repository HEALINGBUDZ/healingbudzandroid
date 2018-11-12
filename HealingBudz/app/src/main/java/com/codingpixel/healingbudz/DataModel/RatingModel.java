package com.codingpixel.healingbudz.DataModel;

/**
 * Created by macmini on 20/12/2017.
 */

public class RatingModel {
    public Integer id;
    public Integer strainId;
    public Integer strainReviewId;
    public Integer ratedBy;
    public Integer rating;
    public String createdAt;

    public String updatedAt;

    public RatingModel() {
    }

    public RatingModel(Integer id, Integer rating) {
        this.id = id;
        this.rating = rating;
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

    public Integer getStrainReviewId() {
        return strainReviewId;
    }

    public void setStrainReviewId(Integer strainReviewId) {
        this.strainReviewId = strainReviewId;
    }

    public Integer getRatedBy() {
        return ratedBy;
    }

    public void setRatedBy(Integer ratedBy) {
        this.ratedBy = ratedBy;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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
