package com.codingpixel.healingbudz.DataModel;

/**
 * Created by jawadali on 12/20/17.
 */

public class BudzMapSpecialProducts {
    int id;
    int user_id;
    int sub_user_id;
    String  title;
    String message;
    String validity_date;
    String image;
    double lat;
    double lng;
    String zip_code;
    String public_location;
    String created_at;
    String updated_at;
    boolean isSaved;

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
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
}
