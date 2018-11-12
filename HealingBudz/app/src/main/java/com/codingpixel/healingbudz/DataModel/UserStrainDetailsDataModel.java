package com.codingpixel.healingbudz.DataModel;


public class UserStrainDetailsDataModel {
    int id;
    int strain_id;
    int user_id;
    int indica;
    int sativa;
    String genetics;
    String cross_breed;
    String growing;
    int plant_height;
    int flowering_time;
    int min_fahren_temp;
    int max_fahren_temp;
    int min_celsius_temp;
    int max_celsius_temp;
    String yeild;
    String climate;
    String note;
    int get_user_like_count;
    String description;
    String created_at;
    String updated_at;
    int get_likes_count;
    String user_name;
    String user_image_path;
    int user_point;
    String avatar;
    String special_icon;
    String min_CBD, max_CBD, min_THC, max_THC;

    public void setUser_point(int user_point) {
        this.user_point = user_point;
    }

    public int getUser_point() {
        return user_point;
    }

    public String getSpecial_icon() {
        if (special_icon != null && special_icon.length() > 6)
            return special_icon;
        else {
            return "";
        }
    }

    public void setSpecial_icon(String special_icon) {
        this.special_icon = special_icon;
    }

    public String getMin_CBD() {
        return min_CBD;
    }

    public void setMin_CBD(String min_CBD) {
        this.min_CBD = min_CBD;
    }

    public String getMax_CBD() {
        return max_CBD;
    }

    public void setMax_CBD(String max_CBD) {
        this.max_CBD = max_CBD;
    }

    public String getMin_THC() {
        return min_THC;
    }

    public void setMin_THC(String min_THC) {
        this.min_THC = min_THC;
    }

    public String getMax_THC() {
        return max_THC;
    }

    public void setMax_THC(String max_THC) {
        this.max_THC = max_THC;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGet_user_like_count() {
        return get_user_like_count;
    }

    public void setGet_user_like_count(int get_user_like_count) {
        this.get_user_like_count = get_user_like_count;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image_path() {
        if (user_image_path != null && !user_image_path.equalsIgnoreCase("null") && user_image_path.length() > 6) {
            return user_image_path;
        } else {
            return avatar;
        }
    }

    public void setUser_image_path(String user_image_path) {
        this.user_image_path = user_image_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStrain_id() {
        return strain_id;
    }

    public void setStrain_id(int strain_id) {
        this.strain_id = strain_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIndica() {
        return indica;
    }

    public void setIndica(int indica) {
        this.indica = indica;
    }

    public int getSativa() {
        return sativa;
    }

    public void setSativa(int sativa) {
        this.sativa = sativa;
    }

    public String getGenetics() {
        return genetics;
    }

    public void setGenetics(String genetics) {
        this.genetics = genetics;
    }

    public String getCross_breed() {
        return cross_breed;
    }

    public void setCross_breed(String cross_breed) {
        this.cross_breed = cross_breed;
    }

    public String getGrowing() {
        return growing;
    }

    public void setGrowing(String growing) {
        this.growing = growing;
    }

    public int getPlant_height() {
        return plant_height;
    }

    public void setPlant_height(int plant_height) {
        this.plant_height = plant_height;
    }

    public int getFlowering_time() {
        return flowering_time;
    }

    public void setFlowering_time(int flowering_time) {
        this.flowering_time = flowering_time;
    }

    public int getMin_fahren_temp() {
        return min_fahren_temp;
    }

    public void setMin_fahren_temp(int min_fahren_temp) {
        this.min_fahren_temp = min_fahren_temp;
    }

    public int getMax_fahren_temp() {
        return max_fahren_temp;
    }

    public void setMax_fahren_temp(int max_fahren_temp) {
        this.max_fahren_temp = max_fahren_temp;
    }

    public int getMin_celsius_temp() {
        return min_celsius_temp;
    }

    public void setMin_celsius_temp(int min_celsius_temp) {
        this.min_celsius_temp = min_celsius_temp;
    }

    public int getMax_celsius_temp() {
        return max_celsius_temp;
    }

    public void setMax_celsius_temp(int max_celsius_temp) {
        this.max_celsius_temp = max_celsius_temp;
    }

    public String getYeild() {
        return yeild;
    }

    public void setYeild(String yeild) {
        this.yeild = yeild;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getNote() {
        if (note != null)
            if (note.equalsIgnoreCase("null")) {
                return "No notes added.";
            } else {
                return note;
            }
        else {
            return "No notes added.";
        }
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getGet_likes_count() {
        return get_likes_count;
    }

    public void setGet_likes_count(int get_likes_count) {
        this.get_likes_count = get_likes_count;
    }
}
