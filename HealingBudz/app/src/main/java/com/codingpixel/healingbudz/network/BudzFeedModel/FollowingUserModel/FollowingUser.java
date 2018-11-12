
package com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.codingpixel.healingbudz.network.model.URL;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.linkedin.android.spyglass.mentions.Mentionable;

import java.io.Serializable;

public class FollowingUser implements Mentionable, Serializable {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("followed_id")
    @Expose
    private Integer followedId;
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
    @SerializedName("password")
    @Expose
    private String password;
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
    @SerializedName("point_redeem")
    @Expose
    private Integer pointRedeem;
    @SerializedName("remember_token")
    @Expose
    private String rememberToken;
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

    public FollowingUser() {
        isSubUser = false;
    }

    private boolean isSubUser;

    public boolean isSubUser() {
        return isSubUser;
    }

    public void setSubUser(boolean subUser) {
        isSubUser = subUser;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFollowedId() {
        return followedId;
    }

    public void setFollowedId(Integer followedId) {
        this.followedId = followedId;
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
        if (lastName == null || lastName.toLowerCase().trim().equals("null")) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getAvatar() {
        return URL.images_baseurl + avatar;
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
        if (bio == null || bio.trim().equalsIgnoreCase("null")) {
            return "";
        }
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

    public Object getFbId() {
        return fbId;
    }

    public Integer getPoints() {
        if (points == null) {
            return 0;
        }
        return points;
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

    public String getImagePath() {
        if (imagePath == null || imagePath.trim().equalsIgnoreCase("null") || imagePath.trim().length() < 4) {
            return getAvatar();
        } else if (imagePath != null && (imagePath.contains("google.com") ||imagePath.contains("http") ||imagePath.contains("https") || imagePath.contains("googleusercontent.com") || imagePath.contains("facebook.com"))) {
            return imagePath;
        } else {
            return URL.images_baseurl + imagePath;
        }
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

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
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

    @NonNull
    @Override
    public String getTextForDisplayMode(MentionDisplayMode mode) {
        switch (mode) {
            case FULL:
                return getFirstName() + " " + getLastName();
            case PARTIAL:
            case NONE:
            default:
                return "";
        }
    }

    @Override
    public MentionDeleteStyle getDeleteStyle() {
        return MentionDeleteStyle.PARTIAL_NAME_DELETE;
    }

    @Override
    public int getSuggestibleId() {
        return userId;
    }

    @Override
    public String getSuggestiblePrimaryText() {
        if (getLastName().trim().length() > 0) {
//            return getFirstName().replace(" ", "_") + "_" + getLastName().replace(" ", "_");
            return getFirstName() + " " + getLastName();
        }
        return getFirstName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getFirstName());
    }

    public FollowingUser(Parcel in) {
        firstName = in.readString();
        isSubUser = false;
    }

    public static final Parcelable.Creator<FollowingUser> CREATOR
            = new Parcelable.Creator<FollowingUser>() {
        public FollowingUser createFromParcel(Parcel in) {
            return new FollowingUser(in);
        }

        public FollowingUser[] newArray(int size) {
            return new FollowingUser[size];
        }
    };
}
