package com.codingpixel.healingbudz.DataModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by codingpixel on 02/08/2017.
 */

public class HomeQAfragmentDataModel implements Serializable {
    String user_name;
    String user_name_dscription;
    String Question;
    String Question_description;
    String user_photo;
    String avatar;
    String special_icon;
    int isAnswered;
    int user_points;
    String user_location;
    int show_ads;
    boolean isFavorite;
    int AnswerCount;
    int id;
    int user_id;
    int get_user_likes_count;
    int get_user_flag_count;
    String type;
    String created_at;
    String updated_at;
    int user_notify;
    private ArrayList<QuestionAnswersDataModel.Attachment> attachments;

    public ArrayList<QuestionAnswersDataModel.Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<QuestionAnswersDataModel.Attachment> attachments) {
        this.attachments = attachments;
    }

    public int getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(int isAnswered) {
        this.isAnswered = isAnswered;
    }

    public String getSpecial_icon() {
        if (special_icon != null && special_icon.length() > 5)
            return special_icon;
        else {
            return "";
        }
    }

    public void setSpecial_icon(String special_icon) {
        this.special_icon = special_icon;
    }

    public int getShow_ads() {
        return show_ads;
    }

    public void setShow_ads(int show_ads) {
        this.show_ads = show_ads;
    }

    public int getUser_notify() {
        return user_notify;
    }

    public void setUser_notify(int user_notify) {
        this.user_notify = user_notify;
    }

    public int getUser_points() {
        return user_points;
    }

    public void setUser_points(int user_points) {
        this.user_points = user_points;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name_dscription() {
        return user_name_dscription;
    }

    public void setUser_name_dscription(String user_name_dscription) {
        this.user_name_dscription = user_name_dscription;
    }

    public String getQuestion_description() {
        return Question_description;
    }

    public void setQuestion_description(String question_description) {
        Question_description = question_description;
    }

    public String getUser_location() {
        return user_location;
    }

    public void setUser_location(String user_location) {
        this.user_location = user_location;
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

    public int getGet_user_likes_count() {
        return get_user_likes_count;
    }

    public void setGet_user_likes_count(int get_user_likes_count) {
        this.get_user_likes_count = get_user_likes_count;
    }

    public int getGet_user_flag_count() {
        return get_user_flag_count;
    }

    public void setGet_user_flag_count(int get_user_flag_count) {
        this.get_user_flag_count = get_user_flag_count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getuser_name() {
        return user_name;
    }

    public void setuser_name(String user_name) {
        user_name = user_name;
    }

    public String getuser_name_dscription() {
        return user_name_dscription;
    }

    public void setuser_name_dscription(String user_name_dscription) {
        user_name_dscription = user_name_dscription;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getUser_photo() {
        if (user_photo != null && !user_photo.equalsIgnoreCase("null") && user_photo.length() > 6) {
            return user_photo;
        } else {
            return getAvatar();
        }
//        return user_photo;
    }

    public String getAvatar() {
        if (avatar != null)
            return avatar;
        else {
            return "";
        }
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getAnswerCount() {
        return AnswerCount;
    }

    public void setAnswerCount(int answerCount) {
        AnswerCount = answerCount;
    }

//    public HomeQAfragmentDataModel(String user_name, String user_name_dscription, String question, String question_descriptio, String user_photo, boolean isFavorite, int answerCount) {
//        this.user_name = user_name;
//        this.user_name_dscription = user_name_dscription;
//        this.Question = question;
//        this.Question_description = question_descriptio;
//        this.user_photo = user_photo;
//        this.isFavorite = isFavorite;
//        this.AnswerCount = answerCount;
//
//    }

    public HomeQAfragmentDataModel(int id) {
        this.id = id;
    }

    public HomeQAfragmentDataModel() {
        super();
    }
}
