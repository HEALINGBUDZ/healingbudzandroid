package com.codingpixel.healingbudz.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class JournalDetailsExpandAbleData implements Parcelable {
    int id;
    int user_id;
    int journal_id;
    int strain_id;
    String title;
    String date;
    String feeling;
    String description;
    String created_at;
    String new_date;
    int year;
    int month;
    ArrayList<JournalTagsDataModal> tags;
    ArrayList<String> get_image_attachments;
    ArrayList<HealingBudGalleryModel> get_video_attachments;
    int  get_likes_count;
    int  get_dis_likes_count;
    boolean isCurrentUserLike;
    boolean isCurrentUserDisLike;
    private String name;
    String Starin_detail;
    String strain_title;

    public String getStrain_title() {
        return strain_title;
    }

    public void setStrain_title(String strain_title) {
        this.strain_title = strain_title;
    }

    public boolean isCurrentUserLike() {
        return isCurrentUserLike;
    }

    public String getStarin_detail() {
        return Starin_detail;
    }

    public void setStarin_detail(String starin_detail) {
        Starin_detail = starin_detail;
    }

    public void setCurrentUserLike(boolean currentUserLike) {
        isCurrentUserLike = currentUserLike;
    }

    public boolean isCurrentUserDisLike() {
        return isCurrentUserDisLike;
    }

    public void setCurrentUserDisLike(boolean currentUserDisLike) {
        isCurrentUserDisLike = currentUserDisLike;
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

    public int getJournal_id() {
        return journal_id;
    }

    public void setJournal_id(int journal_id) {
        this.journal_id = journal_id;
    }

    public int getStrain_id() {
        return strain_id;
    }

    public void setStrain_id(int strain_id) {
        this.strain_id = strain_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getNew_date() {
        return new_date;
    }

    public void setNew_date(String new_date) {
        this.new_date = new_date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    public ArrayList<JournalTagsDataModal> getTags() {
        return tags;
    }

    public void setTags(ArrayList<JournalTagsDataModal> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getGet_image_attachments() {
        return get_image_attachments;
    }

    public void setGet_image_attachments(ArrayList<String> get_image_attachments) {
        this.get_image_attachments = get_image_attachments;
    }

    public ArrayList<HealingBudGalleryModel> getGet_video_attachments() {
        return get_video_attachments;
    }

    public void setGet_video_attachments(ArrayList<HealingBudGalleryModel> get_video_attachments) {
        this.get_video_attachments = get_video_attachments;
    }

    public int getGet_likes_count() {
        return get_likes_count;
    }

    public void setGet_likes_count(int get_likes_count) {
        this.get_likes_count = get_likes_count;
    }

    public int getGet_dis_likes_count() {
        return get_dis_likes_count;
    }

    public void setGet_dis_likes_count(int get_dis_likes_count) {
        this.get_dis_likes_count = get_dis_likes_count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private boolean isFavorite;

    public JournalDetailsExpandAbleData() {
       super();
    }

    public JournalDetailsExpandAbleData(String name, boolean isFavorite) {
        this.name = name;
        this.isFavorite = isFavorite;
    }

    protected JournalDetailsExpandAbleData(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalDetailsExpandAbleData)) return false;

        JournalDetailsExpandAbleData artist = (JournalDetailsExpandAbleData) o;

        if (isFavorite() != artist.isFavorite()) return false;
        return getName() != null ? getName().equals(artist.getName()) : artist.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (isFavorite() ? 1 : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JournalDetailsExpandAbleData> CREATOR = new Creator<JournalDetailsExpandAbleData>() {
        @Override
        public JournalDetailsExpandAbleData createFromParcel(Parcel in) {
            return new JournalDetailsExpandAbleData(in);
        }

        @Override
        public JournalDetailsExpandAbleData[] newArray(int size) {
            return new JournalDetailsExpandAbleData[size];
        }
    };
}

