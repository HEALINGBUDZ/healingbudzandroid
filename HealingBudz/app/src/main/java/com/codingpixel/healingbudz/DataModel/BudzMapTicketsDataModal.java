package com.codingpixel.healingbudz.DataModel;

import java.io.Serializable;

/**
 * Created by jawadali on 12/20/17.
 */

public class BudzMapTicketsDataModal implements Serializable {
    int id;
    int sub_user_id;
    String title;
    String price;
    String image;
    String created_at;
    String updated_at;
    String linkee;

    public String getLinkee() {
        if (linkee == null || linkee.equalsIgnoreCase("null")) {
            return "";
        }
        return linkee;
    }

    public void setLinkee(String linkee) {
        this.linkee = linkee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        if (image == null) {
            return "";
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
