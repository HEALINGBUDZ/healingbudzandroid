
package com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost;

import com.codingpixel.healingbudz.network.model.URL;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SharedUser implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("zip_code")
    @Expose
    private Integer zipCode;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("user_type")
    @Expose
    private Integer userType;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("state_id")
    @Expose
    private Object stateId;
    @SerializedName("google_id")
    @Expose
    private String googleId;
    @SerializedName("fb_id")
    @Expose
    private String fbId;
    @SerializedName("is_web")
    @Expose
    private Integer isWeb;
    @SerializedName("show_my_save")
    @Expose
    private Integer showMySave;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("point_redeem")
    @Expose
    private Integer pointRedeem;
    @SerializedName("emaillestorecode")
    @Expose
    private Object emaillestorecode;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("special_icon")
    @Expose
    private String special_icon;

    public String getSpecial_icon() {
        if (special_icon != null && special_icon.length() > 5)
            return URL.images_baseurl + special_icon;
        else {
            return "";
        }
    }

    public void setSpecial_icon(String special_icon) {
        this.special_icon = special_icon;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getStateId() {
        return stateId;
    }

    public void setStateId(Object stateId) {
        this.stateId = stateId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public Integer getIsWeb() {
        return isWeb;
    }

    public void setIsWeb(Integer isWeb) {
        this.isWeb = isWeb;
    }

    public Integer getShowMySave() {
        return showMySave;
    }

    public void setShowMySave(Integer showMySave) {
        this.showMySave = showMySave;
    }

    public Integer getPoints() {
        if (points == null) {
            return 0;
        }
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getPointRedeem() {
        return pointRedeem;
    }

    public void setPointRedeem(Integer pointRedeem) {
        this.pointRedeem = pointRedeem;
    }

    public Object getEmaillestorecode() {
        return emaillestorecode;
    }

    public void setEmaillestorecode(Object emaillestorecode) {
        this.emaillestorecode = emaillestorecode;
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
