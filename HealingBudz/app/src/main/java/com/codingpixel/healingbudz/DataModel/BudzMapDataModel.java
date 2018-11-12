package com.codingpixel.healingbudz.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jawadali on 8/17/17.
 */

public class BudzMapDataModel implements Parcelable {
    int Type;
    String Title;
    String Distance;
    int Rating;
    int Reviews;
    boolean isFeatureBushiness;
    int icon;
    boolean isDelivery;
    boolean isOrganic;
    double latitude;
    double longitude;
    int user_id;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int business_type_id;
    String  logo;
    String  banner;
    String  location;
    String  description;
    String  phone;
    String  web;
    String  facebook;
    String  twitter;
    String  instagram;
    String  created_at;
    String  updated_at;
    String  stripe_id;
    String  card_brand;
    String  card_last_four;
    String  trial_ends_at;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBusiness_type_id() {
        return business_type_id;
    }

    public void setBusiness_type_id(int business_type_id) {
        this.business_type_id = business_type_id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStripe_id() {
        return stripe_id;
    }

    public void setStripe_id(String stripe_id) {
        this.stripe_id = stripe_id;
    }

    public String getCard_brand() {
        return card_brand;
    }

    public void setCard_brand(String card_brand) {
        this.card_brand = card_brand;
    }

    public String getCard_last_four() {
        return card_last_four;
    }

    public void setCard_last_four(String card_last_four) {
        this.card_last_four = card_last_four;
    }

    public String getTrial_ends_at() {
        return trial_ends_at;
    }

    public void setTrial_ends_at(String trial_ends_at) {
        this.trial_ends_at = trial_ends_at;
    }

    public BudzMapDataModel(){

    }
    public BudzMapDataModel(int type, String title, String distance , int rating , int reviews , boolean isFeatureBushiness ,
                            int icon , boolean isDelivery , boolean isOrganic , double latitude , double longitude){
        this.Type = type ;
        this.Title = title ;
        this.Distance = distance;
        this.Rating = rating ;
        this.Reviews = reviews ;
        this.isFeatureBushiness = isFeatureBushiness;
        this.isDelivery = isDelivery;
        this.icon = icon;
        this.isOrganic = isOrganic ;
        this.latitude = latitude ;
        this.longitude= longitude;

    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public int getReviews() {
        return Reviews;
    }

    public void setReviews(int reviews) {
        Reviews = reviews;
    }

    public boolean isFeatureBushiness() {
        return isFeatureBushiness;
    }

    public void setFeatureBushiness(boolean featureBushiness) {
        isFeatureBushiness = featureBushiness;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isDelivery() {
        return isDelivery;
    }

    public void setDelivery(boolean delivery) {
        isDelivery = delivery;
    }

    public boolean isOrganic() {
        return isOrganic;
    }

    public void setOrganic(boolean organic) {
        isOrganic = organic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Type);
        dest.writeString(this.Title);
        dest.writeString(this.Distance);
        dest.writeInt(this.Rating);
        dest.writeInt(this.Reviews);
        dest.writeByte(this.isFeatureBushiness ? (byte) 1 : (byte) 0);
        dest.writeInt(this.icon);
        dest.writeByte(this.isDelivery ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOrganic ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.user_id);
        dest.writeInt(this.id);
        dest.writeInt(this.business_type_id);
        dest.writeString(this.logo);
        dest.writeString(this.banner);
        dest.writeString(this.location);
        dest.writeString(this.description);
        dest.writeString(this.phone);
        dest.writeString(this.web);
        dest.writeString(this.facebook);
        dest.writeString(this.twitter);
        dest.writeString(this.instagram);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.stripe_id);
        dest.writeString(this.card_brand);
        dest.writeString(this.card_last_four);
        dest.writeString(this.trial_ends_at);
    }

    protected BudzMapDataModel(Parcel in) {
        this.Type = in.readInt();
        this.Title = in.readString();
        this.Distance = in.readString();
        this.Rating = in.readInt();
        this.Reviews = in.readInt();
        this.isFeatureBushiness = in.readByte() != 0;
        this.icon = in.readInt();
        this.isDelivery = in.readByte() != 0;
        this.isOrganic = in.readByte() != 0;
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.user_id = in.readInt();
        this.id = in.readInt();
        this.business_type_id = in.readInt();
        this.logo = in.readString();
        this.banner = in.readString();
        this.location = in.readString();
        this.description = in.readString();
        this.phone = in.readString();
        this.web = in.readString();
        this.facebook = in.readString();
        this.twitter = in.readString();
        this.instagram = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.stripe_id = in.readString();
        this.card_brand = in.readString();
        this.card_last_four = in.readString();
        this.trial_ends_at = in.readString();
    }

    public static final Parcelable.Creator<BudzMapDataModel> CREATOR = new Parcelable.Creator<BudzMapDataModel>() {
        @Override
        public BudzMapDataModel createFromParcel(Parcel source) {
            return new BudzMapDataModel(source);
        }

        @Override
        public BudzMapDataModel[] newArray(int size) {
            return new BudzMapDataModel[size];
        }
    };
}
