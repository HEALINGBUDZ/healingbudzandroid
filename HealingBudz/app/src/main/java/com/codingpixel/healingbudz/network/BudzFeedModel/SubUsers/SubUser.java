package com.codingpixel.healingbudz.network.BudzFeedModel.SubUsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by M_Muzammil Sharif on 02-Apr-18.
 */
public class SubUser implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("business_type_id")
    @Expose
    private Integer businessTypeId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("is_organic")
    @Expose
    private Integer isOrganic;
    @SerializedName("is_delivery")
    @Expose
    private Integer isDelivery;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("web")
    @Expose
    private String web;
    @SerializedName("facebook")
    @Expose
    private Object facebook;
    @SerializedName("twitter")
    @Expose
    private Object twitter;
    @SerializedName("instagram")
    @Expose
    private Object instagram;
    @SerializedName("insurance_accepted")
    @Expose
    private Object insuranceAccepted;
    @SerializedName("office_policies")
    @Expose
    private Object officePolicies;
    @SerializedName("visit_requirements")
    @Expose
    private Object visitRequirements;
    @SerializedName("menu_tab_count")
    @Expose
    private Integer menuTabCount;
    @SerializedName("purchase_ticket_count")
    @Expose
    private Integer purchaseTicketCount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("stripe_id")
    @Expose
    private String stripeId;
    @SerializedName("card_brand")
    @Expose
    private String cardBrand;
    @SerializedName("card_last_four")
    @Expose
    private Integer cardLastFour;
    @SerializedName("trial_ends_at")
    @Expose
    private Object trialEndsAt;

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

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Integer getIsOrganic() {
        return isOrganic;
    }

    public void setIsOrganic(Integer isOrganic) {
        this.isOrganic = isOrganic;
    }

    public Integer getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(Integer isDelivery) {
        this.isDelivery = isDelivery;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public Object getFacebook() {
        return facebook;
    }

    public void setFacebook(Object facebook) {
        this.facebook = facebook;
    }

    public Object getTwitter() {
        return twitter;
    }

    public void setTwitter(Object twitter) {
        this.twitter = twitter;
    }

    public Object getInstagram() {
        return instagram;
    }

    public void setInstagram(Object instagram) {
        this.instagram = instagram;
    }

    public Object getInsuranceAccepted() {
        return insuranceAccepted;
    }

    public void setInsuranceAccepted(Object insuranceAccepted) {
        this.insuranceAccepted = insuranceAccepted;
    }

    public Object getOfficePolicies() {
        return officePolicies;
    }

    public void setOfficePolicies(Object officePolicies) {
        this.officePolicies = officePolicies;
    }

    public Object getVisitRequirements() {
        return visitRequirements;
    }

    public void setVisitRequirements(Object visitRequirements) {
        this.visitRequirements = visitRequirements;
    }

    public Integer getMenuTabCount() {
        return menuTabCount;
    }

    public void setMenuTabCount(Integer menuTabCount) {
        this.menuTabCount = menuTabCount;
    }

    public Integer getPurchaseTicketCount() {
        return purchaseTicketCount;
    }

    public void setPurchaseTicketCount(Integer purchaseTicketCount) {
        this.purchaseTicketCount = purchaseTicketCount;
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

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public Integer getCardLastFour() {
        return cardLastFour;
    }

    public void setCardLastFour(Integer cardLastFour) {
        this.cardLastFour = cardLastFour;
    }

    public Object getTrialEndsAt() {
        return trialEndsAt;
    }

    public void setTrialEndsAt(Object trialEndsAt) {
        this.trialEndsAt = trialEndsAt;
    }
}
