package com.codingpixel.healingbudz.DataModel;

public class JournalTagsDataModal {

    int id;
    String  title;
    int is_approved;
    boolean isTagClicked;
    String created_at;

    public int getId() {
        return id;
    }

    public boolean isTagClicked() {
        return isTagClicked;
    }

    public void setTagClicked(boolean tagClicked) {
        isTagClicked = tagClicked;
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

    public int getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(int is_approved) {
        this.is_approved = is_approved;
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

    String updated_at;
}
