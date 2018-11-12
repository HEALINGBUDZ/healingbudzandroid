package com.codingpixel.healingbudz.DataModel;

/**
 * Created by codingpixel on 10/08/2017.
 */

public class BuzzFeedDataModel {
    int id;
    int user_id;
    String on_user;
    String type;
    String description;
    String text;
    String model;
    String notification_text;
    String updated_at;
    String created_at;
    int type_id;
    int is_read;
    int sub_user_id;
    private String typeSubId;

    public int getSub_user_id() {
        return sub_user_id;
    }

    public void setSub_user_id(int sub_user_id) {
        this.sub_user_id = sub_user_id;
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

    public String getOn_user() {
        return on_user;
    }

    public void setOn_user(String on_user) {
        this.on_user = on_user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public void setNotification_text(String notification_text) {
        this.notification_text = notification_text;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public void setTypeSubId(String typeSubId) {
        this.typeSubId = typeSubId;
    }

    public String getTypeSubId() {
        return typeSubId;
    }
}
