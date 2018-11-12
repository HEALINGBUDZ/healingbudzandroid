package com.codingpixel.healingbudz.DataModel;

import java.util.ArrayList;

/**
 * Created by jawadali on 8/30/17.
 */

public class JournalFragmentDataModel {
    private boolean isSlideOpen;
    private boolean isFavorite;
    private boolean isFollow;
    private int id;
    private String title;
    private int is_public;
    private String created_at;
    private String updated_at;
    private int get_tags_count;
    private int get_followers_count;
    private int events_count;
    private String user_first_name;
    private String user_image_path;
    private int user_id;
    private String avatar;
    private ArrayList<JournalEvent> journalEvents ;

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public int getId() {
        return id;
    }

    public ArrayList<JournalEvent> getJournalEvents() {
        return journalEvents;
    }

    public void setJournalEvents(ArrayList<JournalEvent> journalEvents) {
        this.journalEvents = journalEvents;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIs_public() {
        return is_public;
    }

    public void setIs_public(int is_public) {
        this.is_public = is_public;
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

    public int getGet_tags_count() {
        return get_tags_count;
    }

    public void setGet_tags_count(int get_tags_count) {
        this.get_tags_count = get_tags_count;
    }

    public int getGet_followers_count() {
        return get_followers_count;
    }

    public void setGet_followers_count(int get_followers_count) {
        this.get_followers_count = get_followers_count;
    }

    public int getEvents_count() {
        return events_count;
    }

    public void setEvents_count(int events_count) {
        this.events_count = events_count;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isSlideOpen() {
        return isSlideOpen;
    }

    public void setSlideOpen(boolean slideOpen) {
        isSlideOpen = slideOpen;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public JournalFragmentDataModel() {

    }

    public JournalFragmentDataModel(boolean isFavorite, boolean isSlideOpen) {
        this.isFavorite = isFavorite;
        this.isSlideOpen = isSlideOpen;

    }


    public static class JournalEvent{
        String Event_Title;
        String  Event_Date ;
        String  Feeling ;

        public String getEvent_Title() {
            return Event_Title;
        }

        public void setEvent_Title(String event_Title) {
            Event_Title = event_Title;
        }

        public String getEvent_Date() {
            return Event_Date;
        }

        public String getFeeling() {
            return Feeling;
        }

        public void setFeeling(String feeling) {
            Feeling = feeling;
        }

        public void setEvent_Date(String event_Date) {
            Event_Date = event_Date;
        }
    }
}
