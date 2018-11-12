package com.codingpixel.healingbudz.DataModel;

/**
 * Created by macmini on 12/04/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Budz {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sub_user_id")
    @Expose
    private Integer subUserId;
    @SerializedName("strain_id")
    @Expose
    private Integer strainId;
    @SerializedName("type_id")
    @Expose
    private Integer typeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thc")
    @Expose
    private Double thc;
    @SerializedName("cbd")
    @Expose
    private Double cbd;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("logo")
    @Expose
    private Object logo;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("tag_id")
    @Expose
    private Integer tagId;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("tag_title")
    @Expose
    private String tagTitle;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("strain_type")
    @Expose
    private StrainType strainType;
    @SerializedName("images")
    @Expose
    private List<ImageModelStrain> images = null;
    @SerializedName("pricing")
    @Expose
    private List<Pricing> pricing = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(Integer subUserId) {
        this.subUserId = subUserId;
    }

    public Integer getStrainId() {
        return strainId;
    }

    public void setStrainId(Integer strainId) {
        this.strainId = strainId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getThc() {
        return thc;
    }

    public void setThc(Double thc) {
        this.thc = thc;
    }

    public Double getCbd() {
        return cbd;
    }

    public void setCbd(Double cbd) {
        this.cbd = cbd;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getLogo() {
        return logo;
    }

    public void setLogo(Object logo) {
        this.logo = logo;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public StrainType getStrainType() {
        return strainType;
    }

    public void setStrainType(StrainType strainType) {
        this.strainType = strainType;
    }

    public List<ImageModelStrain> getImages() {
        return images;
    }

    public void setImages(List<ImageModelStrain> images) {
        this.images = images;
    }

    public List<Pricing> getPricing() {
        return pricing;
    }

    public void setPricing(List<Pricing> pricing) {
        this.pricing = pricing;
    }

}
