package com.codingpixel.healingbudz.DataModel;

import java.util.ArrayList;

public class GroupsDataModel {

    private String Name;
    private int id;
    private int user_id;
    private String image;
    private String title;
    private String description;
    private int is_private;
    private int get_members_count;
    private int is_admin_count;
    private int is_following_count;
    private String created_at;
    private String updated_at;
    private String group_owner;
    private String group_tags;
    private String group_tags_ids;
    private boolean isCurrentUserAdmin;
    private ArrayList<GroupMembers> groupMembers;





    public ArrayList<GroupMembers> getGroupMembers() {
        return groupMembers;
    }
    public int getIs_following_count() {
        return is_following_count;
    }

    public void setIs_following_count(int is_following_count) {
        this.is_following_count = is_following_count;
    }

    public boolean isCurrentUserAdmin() {
        return isCurrentUserAdmin;
    }

    public void setCurrentUserAdmin(boolean currentUserAdmin) {
        isCurrentUserAdmin = currentUserAdmin;
    }
    public void setGroupMembers(ArrayList<GroupMembers> groupMembers) {
        this.groupMembers = groupMembers;
    }
    public String getGroup_tags_ids() {
        return group_tags_ids;
    }

    public void setGroup_tags_ids(String group_tags_ids) {
        this.group_tags_ids = group_tags_ids;
    }

    public String getGroup_owner() {
        return group_owner;
    }

    public void setGroup_owner(String group_owner) {
        this.group_owner = group_owner;
    }

    public String getGroup_tags() {
        return group_tags;
    }

    public void setGroup_tags(String group_tags) {
        this.group_tags = group_tags;
    }

    public GroupsDataModel(String name, String no_of_budz) {
        this.Name = name;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIs_private() {
        return is_private;
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
    }

    public int getGet_members_count() {
        return get_members_count;
    }

    public void setGet_members_count(int get_members_count) {
        this.get_members_count = get_members_count;
    }

    public int getIs_admin_count() {
        return is_admin_count;
    }

    public void setIs_admin_count(int is_admin_count) {
        this.is_admin_count = is_admin_count;
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

    public GroupsDataModel() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public static class GroupMembers {
        String Name;
        int user_id;
        int group_id;
        String image_path;
        boolean isAdmin;

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean admin) {
            isAdmin = admin;
        }

        public String getName() {
            return Name;

        }

        public void setName(String name) {
            Name = name;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }
    }
}
