package com.codingpixel.healingbudz.DataModel;

/**
 * Created by jawadali on 9/27/17.
 */

public class MessagesDataModel {
    int id;
    boolean isBudz;
    boolean isBudzPeople;
    int budzType;
    String budzName;
    String budzBusinessName;
    int budz_id;
    int member_count;
    int sender_id;
    int receiver_id;
    int last_message_id;
    int sender_deleted;
    int receiver_deleted;
    String created_at;
    String updated_at;
    int messages_count;
    String sender_first_name;
    String sender_image_path;
    String sender_avatar;
    int sender_point;
    String receiver_avatar;
    String receiver_image_path;
    String receiver_first_name;
    int recev_point;
    String special_icon_rec;
    String special_icon_sen;
    boolean isMain = false;
    int save_count;
    int is_online_count;
    int is_online_count_rec;

    public int getIs_online_count_rec() {
        return is_online_count_rec;
    }

    public void setIs_online_count_rec(int is_online_count_rec) {
        this.is_online_count_rec = is_online_count_rec;
    }

    public int getIs_online_count() {
        return is_online_count;
    }

    public void setIs_online_count(int is_online_count) {
        this.is_online_count = is_online_count;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public int getSave_count() {
        return save_count;
    }

    public void setSave_count(int save_count) {
        this.save_count = save_count;
    }

    public int getRecev_point() {
        return recev_point;
    }

    public int getSender_point() {
        return sender_point;
    }

    public void setRecev_point(int recev_point) {
        this.recev_point = recev_point;
    }

    public void setSender_point(int sender_point) {
        this.sender_point = sender_point;
    }

    public String getSpecial_icon_rec() {
        if (special_icon_rec != null)
            return special_icon_rec;
        else {
            return "";
        }
    }

    public void setSpecial_icon_rec(String special_icon_rec) {
        this.special_icon_rec = special_icon_rec;
    }

    public String getSpecial_icon_sen() {
        if (special_icon_sen != null)
            return special_icon_sen;
        else {
            return "";
        }
    }

    public void setSpecial_icon_sen(String special_icon_sen) {
        this.special_icon_sen = special_icon_sen;
    }

    public String getBudzBusinessName() {
        return budzBusinessName;
    }

    public boolean isBudzPeople() {
        return isBudzPeople;
    }

    public void setBudzPeople(boolean budzPeople) {
        isBudzPeople = budzPeople;
    }

    public void setBudzBusinessName(String budzBusinessName) {
        this.budzBusinessName = budzBusinessName;
    }

    public boolean isBudz() {
        return isBudz;
    }

    public void setBudz(boolean budz) {
        isBudz = budz;
    }

    public int getBudzType() {
        return budzType;
    }

    public void setBudzType(int budzType) {
        this.budzType = budzType;
    }

    public String getBudzName() {
        return budzName;
    }

    public void setBudzName(String budzName) {
        this.budzName = budzName;
    }

    public int getBudz_id() {
        return budz_id;
    }

    public void setBudz_id(int budz_id) {
        this.budz_id = budz_id;
    }

    public int getMember_count() {
        return member_count;
    }

    public void setMember_count(int member_count) {
        this.member_count = member_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getLast_message_id() {
        return last_message_id;
    }

    public void setLast_message_id(int last_message_id) {
        this.last_message_id = last_message_id;
    }

    public int getSender_deleted() {
        return sender_deleted;
    }

    public void setSender_deleted(int sender_deleted) {
        this.sender_deleted = sender_deleted;
    }

    public int getReceiver_deleted() {
        return receiver_deleted;
    }

    public void setReceiver_deleted(int receiver_deleted) {
        this.receiver_deleted = receiver_deleted;
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

    public int getMessages_count() {
        return messages_count;
    }

    public void setMessages_count(int messages_count) {
        this.messages_count = messages_count;
    }

    public String getSender_first_name() {
        return sender_first_name;
    }

    public void setSender_first_name(String sender_first_name) {
        this.sender_first_name = sender_first_name;
    }

    public String getSender_image_path() {
        if (sender_image_path == null)
            return null;
        return sender_image_path;
//        else return null;
    }

    public void setSender_image_path(String sender_image_path) {
        this.sender_image_path = sender_image_path;
    }

    public String getSender_avatar() {
        return sender_avatar;
    }

    public void setSender_avatar(String sender_avatar) {
        this.sender_avatar = sender_avatar;
    }

    public String getReceiver_avatar() {
        return receiver_avatar;
    }

    public void setReceiver_avatar(String receiver_avatar) {
        this.receiver_avatar = receiver_avatar;
    }

    public String getReceiver_image_path() {
        if (receiver_image_path == null)
            return null;
        return receiver_image_path;
//        else return null;
    }

    public void setReceiver_image_path(String receiver_image_path) {
        this.receiver_image_path = receiver_image_path;
    }

    public String getReceiver_first_name() {
        return receiver_first_name;
    }

    public void setReceiver_first_name(String receiver_first_name) {
        this.receiver_first_name = receiver_first_name;
    }
}
