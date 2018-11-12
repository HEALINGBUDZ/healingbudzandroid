package com.codingpixel.healingbudz.DataModel;

public class ShootOutDataModel {
    int id;
    int user_id;
    int sub_user_id;
    String title;
    String message;
    String validity_date;
    String image;
    double lat;
    double lng;
    String zip_code;
    String public_location;
    String created_at;
    String updated_at;
    String distance;

    String Received_From;
    String User_Image_Path;
    String text;
    String avatar;
    String special_icon;
    int likes_count;
    int userlike_count;
    private int budzSpId;

    public int getBudzSpId() {
        return budzSpId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSpecial_icon() {
        if (special_icon != null && special_icon.length() > 6)
            return special_icon;
        else {
            return "";
        }
    }

    public void setSpecial_icon(String special_icon) {
        this.special_icon = special_icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReceived_From() {
        return Received_From;
    }

    public void setReceived_From(String received_From) {
        Received_From = received_From;
    }

    public String getUser_Image_Path() {
        if (User_Image_Path != null && User_Image_Path.length() > 6)
            return User_Image_Path;
        else {
            return "";
        }

    }

    public void setUser_Image_Path(String user_Image_Path) {
        User_Image_Path = user_Image_Path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSub_user_id() {
        return sub_user_id;
    }

    public void setSub_user_id(int sub_user_id) {
        this.sub_user_id = sub_user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getUserlike_count() {
        return userlike_count;
    }

    public void setUserlike_count(int userlike_count) {
        this.userlike_count = userlike_count;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValidity_date() {
        return validity_date;
    }

    public void setValidity_date(String validity_date) {
        this.validity_date = validity_date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getPublic_location() {
        return public_location;
    }

    public void setPublic_location(String public_location) {
        this.public_location = public_location;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setBudzSpId(int budzSpId) {
        this.budzSpId = budzSpId;

    }
}
