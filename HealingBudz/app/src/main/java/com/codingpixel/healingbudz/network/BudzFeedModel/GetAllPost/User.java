
package com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost;

import com.codingpixel.healingbudz.network.model.URL;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

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
    private Object cover;
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
    private Object googleId;
    @SerializedName("fb_id")
    @Expose
    private Object fbId;
    @SerializedName("is_web")
    @Expose
    private Integer isWeb;
    @SerializedName("show_my_save")
    @Expose
    private Integer showMySave;
    @SerializedName("points")
    @Expose
    private Integer points;
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
        if (lastName == null) {
            return "";
        }
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
        if (imagePath == null || (imagePath != null && imagePath.length() < 5)) {
            return getAvatar();
        } else {
            if (imagePath.contains("facebook.com") ||imagePath.contains("http") ||imagePath.contains("https") || imagePath.contains("google.com") || imagePath.contains("googleusercontent.com")) {
                return imagePath;
            } else {
                return URL.images_baseurl + imagePath;
            }
        }
//        return URL.images_baseurl + imagePath;
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
        if (avatar != null)
            return URL.images_baseurl + avatar;
        else {
            return URL.images_baseurl + "";
        }
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Object getCover() {
        return cover;
    }

    public void setCover(Object cover) {
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

    public Object getGoogleId() {
        return googleId;
    }

    public void setGoogleId(Object googleId) {
        this.googleId = googleId;
    }

    public Object getFbId() {
        return fbId;
    }

    public void setFbId(Object fbId) {
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", zipCode=" + zipCode +
                ", imagePath='" + imagePath + '\'' +
                ", userType=" + userType +
                ", avatar='" + avatar + '\'' +
                ", cover=" + cover +
                ", bio='" + bio + '\'' +
                ", location='" + location + '\'' +
                ", city=" + city +
                ", stateId=" + stateId +
                ", googleId=" + googleId +
                ", fbId=" + fbId +
                ", isWeb=" + isWeb +
                ", showMySave=" + showMySave +
                ", points=" + points +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
