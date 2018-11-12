package com.codingpixel.healingbudz.DataModel;

public class User {
    private int id;
    int notificationCOunt;

    public int getNotificationCOunt() {
        return notificationCOunt;
    }

    public void setNotificationCOunt(int notificationCOunt) {
        this.notificationCOunt = notificationCOunt;
    }

    private int user_id;
    private String special_icon;
    private String device_type;
    private String device_id;
    private double lat;
    private double lng;
    private boolean isPaidBudz;
    private String session_key;
    private String time_zone;
    private String fb_id;
    private String g_id;
    private String created_at;
    private String updated_at;
    private boolean show_budz_popup;
    private String first_name;
    private String last_name;
    private String email;
    private String zip_code;
    private String image_path;
    private String user_type;
    private String avatar;
    private String cover;
    private String bio;
    private String location;
    private int points = 0;

    public String getSpecial_icon() {
        if (special_icon != null)
            return special_icon;
        else {
            return "";
        }
    }

    public void setSpecial_icon(String special_icon) {
        this.special_icon = special_icon;
    }

    public boolean isPaidBudz() {
        return isPaidBudz;
    }

    public void setPaidBudz(boolean paidBudz) {
        isPaidBudz = paidBudz;
    }

    public boolean isShow_budz_popup() {
        return show_budz_popup;
    }

    public void setShow_budz_popup(boolean show_budz_popup) {
        this.show_budz_popup = show_budz_popup;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
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

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        if (last_name == null || last_name.trim().toLowerCase().equals("null")) {
            return "";
        }
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
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

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
