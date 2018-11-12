package com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.folllowkeys;

public class KeywordModel {
    String title;
    int id;
    int is_following_count;

    public KeywordModel(String ti, int idd) {
        title = ti;
        idd = id;
    }

    public KeywordModel(String ti, int idd, int idds) {
        title = ti;
        idd = id;
        is_following_count = idds;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_following_count() {
        return is_following_count;
    }

    public void setIs_following_count(int is_following_count) {
        this.is_following_count = is_following_count;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
