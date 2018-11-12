package com.codingpixel.healingbudz.DataModel;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionAnswersDataModel {
    private int id;
    private int user_id;
    private int question_id;
    private int answer_like_count;
    private int answer_user_like_count;
    private int flag_by_user_count;
    private String answer;
    int user_points;
    boolean isFollow;
    boolean is_edit_count;
    private int idAns;
    String Text;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public int getIdAns() {
        return idAns;
    }

    public boolean isIs_edit_count() {
        return is_edit_count;
    }

    public void setIs_edit_count(boolean is_edit_count) {
        this.is_edit_count = is_edit_count;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public int getUser_points() {
        return user_points;
    }

    public void setUser_points(int user_points) {
        this.user_points = user_points;
    }

    private String Question;
    private String Question_discription;
    private String created_at;
    private String updated_at;
    private String user_first_name;

    public boolean isReportButtonOpen() {
        return isReportButtonOpen;
    }

    public void setReportButtonOpen(boolean reportButtonOpen) {
        isReportButtonOpen = reportButtonOpen;
    }

    private String user_image_path;
    private String user_avatar;
    String special_icon;

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

    private boolean isReportButtonOpen;

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public int getQuestion_get_likes_count() {
        return question_get_likes_count;
    }

    public void setQuestion_get_likes_count(int question_get_likes_count) {
        this.question_get_likes_count = question_get_likes_count;
    }

    public int getQuestion_get_answers_count() {
        return question_get_answers_count;
    }

    public void setQuestion_get_answers_count(int question_get_answers_count) {
        this.question_get_answers_count = question_get_answers_count;
    }

    public int getQuestion_get_flag_count() {
        return question_get_flag_count;
    }

    public void setQuestion_get_flag_count(int question_get_flag_count) {
        this.question_get_flag_count = question_get_flag_count;
    }

    private String userLocation;
    int question_get_likes_count;
    int question_get_answers_count;
    int question_get_flag_count;

    private ArrayList<Attachment> attachments;

    public String getQuestion_discription() {
        return Question_discription;
    }

    public void setQuestion_discription(String question_discription) {
        Question_discription = question_discription;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public ArrayList<Attachment> getAttachments() {
        if (attachments == null) {
            return new ArrayList<>();
        }
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public int getAnswer_user_like_count() {
        return answer_user_like_count;
    }

    public void setAnswer_user_like_count(int answer_user_like_count) {
        this.answer_user_like_count = answer_user_like_count;
    }

    public int getFlag_by_user_count() {
        return flag_by_user_count;
    }

    public void setFlag_by_user_count(int flag_by_user_count) {
        this.flag_by_user_count = flag_by_user_count;
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

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getAnswer_like_count() {
        return answer_like_count;
    }

    public void setAnswer_like_count(int answer_like_count) {
        this.answer_like_count = answer_like_count;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_image_path() {
        return user_image_path;
    }

    public void setUser_image_path(String user_image_path) {
        this.user_image_path = user_image_path;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public ArrayList<String> getGet_attachments() {
        return get_attachments;
    }

    public void setGet_attachments(ArrayList<String> get_attachments) {
        this.get_attachments = get_attachments;
    }

    private ArrayList<String> get_attachments;

    public void setIdAns(int idAns) {
        this.idAns = idAns;
    }

    public static class Attachment implements Serializable {
        int id;
        int answer_id;
        String upload_path;
        String media_type;
        String poster;

        public Attachment() {
        }

        public Attachment(String path) {
            this.upload_path = path;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAnswer_id() {
            return answer_id;
        }

        public void setAnswer_id(int answer_id) {
            this.answer_id = answer_id;
        }

        public String getUpload_path() {
            return upload_path;
        }

        public void setUpload_path(String upload_path) {
            this.upload_path = upload_path;
        }

        public String getMedia_type() {
            return media_type;
        }

        public void setMedia_type(String media_type) {
            this.media_type = media_type;
        }

        public String getPoster() {
            return poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }
    }
}
