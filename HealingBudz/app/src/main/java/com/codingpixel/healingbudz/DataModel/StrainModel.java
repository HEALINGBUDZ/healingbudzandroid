package com.codingpixel.healingbudz.DataModel;

/**
 * Created by macmini on 20/12/2017.
 */

public class StrainModel {
    public Integer id;
    public Integer typeId;
    public String title;
    public String overview;
    public Integer approved;
    public String createdAt;
    public String updatedAt;
    public RatingSumModel ratingSum;

    public StrainModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
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

    public RatingSumModel getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(RatingSumModel ratingSum) {
        this.ratingSum = ratingSum;
    }
}
