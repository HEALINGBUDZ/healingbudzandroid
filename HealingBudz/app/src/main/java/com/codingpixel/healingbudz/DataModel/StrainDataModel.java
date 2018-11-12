package com.codingpixel.healingbudz.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jawadali on 9/7/17.
 */

public class StrainDataModel implements Parcelable {

    int my_strain_id;
    int id;
    int type_id;
    String title;
    String overview;
    int approved;
    int current_user_like;
    int current_user_dis_like;
    int current_user_flag;
    String created_at;
    String updated_at;
    int favorite;
    int get_review_count;
    int get_likes_count;
    int get_dislikes_count;
    String type_title;
    ArrayList<Images> images;
    double rating_sum;
    int strain_id;
    int mathces;

    public int getMy_strain_id() {
        return my_strain_id;
    }

    public void setMy_strain_id(int my_strain_id) {
        this.my_strain_id = my_strain_id;
    }

    public int getMathces() {
        return mathces;
    }

    public void setMathces(int mathces) {
        this.mathces = mathces;
    }

    public int getCurrent_user_dis_like() {
        return current_user_dis_like;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public void setCurrent_user_dis_like(int current_user_dis_like) {
        this.current_user_dis_like = current_user_dis_like;
    }

    public int getCurrent_user_flag() {
        return current_user_flag;
    }

    public void setCurrent_user_flag(int current_user_flag) {
        this.current_user_flag = current_user_flag;
    }

    public int getCurrent_user_like() {
        return current_user_like;
    }

    public void setCurrent_user_like(int current_user_like) {
        this.current_user_like = current_user_like;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
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

    public int getGet_review_count() {
        return get_review_count;
    }

    public void setGet_review_count(int get_review_count) {
        this.get_review_count = get_review_count;
    }

    public int getGet_likes_count() {
        return get_likes_count;
    }

    public void setGet_likes_count(int get_likes_count) {
        this.get_likes_count = get_likes_count;
    }

    public int getGet_dislikes_count() {
        return get_dislikes_count;
    }

    public void setGet_dislikes_count(int get_dislikes_count) {
        this.get_dislikes_count = get_dislikes_count;
    }

    public String getType_title() {
        return type_title;
    }

    public void setType_title(String type_title) {
        this.type_title = type_title;
    }

    public ArrayList<Images> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public double getRating_sum() {
        return rating_sum;
    }

    public void setRating_sum(double rating_sum) {
        this.rating_sum = rating_sum;
    }

    public int getStrain_id() {
        return strain_id;
    }

    public void setStrain_id(int strain_id) {
        this.strain_id = strain_id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    int total;
    private String Name;
    private double Rating;
    private String Alphabetic_keyword;
    private String Reviews;

    public StrainDataModel(String name, double rating, String alphabetic_keyword, String reviews) {
        this.Name = name;
        this.Rating = rating;
        this.Alphabetic_keyword = alphabetic_keyword;
        this.Reviews = reviews;
    }

    public StrainDataModel() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public String getAlphabetic_keyword() {
        return Alphabetic_keyword;
    }

    public void setAlphabetic_keyword(String alphabetic_keyword) {
        Alphabetic_keyword = alphabetic_keyword;
    }

    public String getReviews() {
        return Reviews;
    }

    public void setReviews(String reviews) {
        Reviews = reviews;
    }

    public static class Images {
        int id;
        int strain_id;
        int user_id;
        String image_path;
        int is_approved;
        int is_main;
        String Name;
        int user_rating;

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public int getDis_like_count() {
            return dis_like_count;
        }

        public void setDis_like_count(int dis_like_count) {
            this.dis_like_count = dis_like_count;
        }

        public boolean isIs_current_user_liked() {
            return is_current_user_liked;
        }

        public void setIs_current_user_liked(boolean is_current_user_liked) {
            this.is_current_user_liked = is_current_user_liked;
        }

        public boolean isIs_current_user_dislike() {
            return is_current_user_dislike;
        }

        public void setIs_current_user_dislike(boolean is_current_user_dislike) {
            this.is_current_user_dislike = is_current_user_dislike;
        }

        public boolean isIs_current_user_flaged() {
            return is_current_user_flaged;
        }

        public void setIs_current_user_flaged(boolean is_current_user_flaged) {
            this.is_current_user_flaged = is_current_user_flaged;
        }

        int like_count;
        int dis_like_count;
        boolean is_current_user_liked;
        boolean is_current_user_dislike;
        boolean is_current_user_flaged;

        public int getUser_rating() {
            return user_rating;
        }

        public void setUser_rating(int user_rating) {
            this.user_rating = user_rating;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
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

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public int getIs_approved() {
            return is_approved;
        }

        public void setIs_approved(int is_approved) {
            this.is_approved = is_approved;
        }

        public int getIs_main() {
            return is_main;
        }

        public void setIs_main(int is_main) {
            this.is_main = is_main;
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

        String created_at;
        String updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.my_strain_id);
        dest.writeInt(this.id);
        dest.writeInt(this.type_id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeInt(this.approved);
        dest.writeInt(this.current_user_like);
        dest.writeInt(this.current_user_dis_like);
        dest.writeInt(this.current_user_flag);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeInt(this.favorite);
        dest.writeInt(this.get_review_count);
        dest.writeInt(this.get_likes_count);
        dest.writeInt(this.get_dislikes_count);
        dest.writeString(this.type_title);
        dest.writeList(this.images);
        dest.writeDouble(this.rating_sum);
        dest.writeInt(this.strain_id);
        dest.writeInt(this.mathces);
        dest.writeInt(this.total);
        dest.writeString(this.Name);
        dest.writeDouble(this.Rating);
        dest.writeString(this.Alphabetic_keyword);
        dest.writeString(this.Reviews);
    }

    protected StrainDataModel(Parcel in) {
        this.my_strain_id = in.readInt();
        this.id = in.readInt();
        this.type_id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.approved = in.readInt();
        this.current_user_like = in.readInt();
        this.current_user_dis_like = in.readInt();
        this.current_user_flag = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.favorite = in.readInt();
        this.get_review_count = in.readInt();
        this.get_likes_count = in.readInt();
        this.get_dislikes_count = in.readInt();
        this.type_title = in.readString();
        this.images = new ArrayList<Images>();
        in.readList(this.images, Images.class.getClassLoader());
        this.rating_sum = in.readDouble();
        this.strain_id = in.readInt();
        this.mathces = in.readInt();
        this.total = in.readInt();
        this.Name = in.readString();
        this.Rating = in.readDouble();
        this.Alphabetic_keyword = in.readString();
        this.Reviews = in.readString();
    }

    public static final Parcelable.Creator<StrainDataModel> CREATOR = new Parcelable.Creator<StrainDataModel>() {
        @Override
        public StrainDataModel createFromParcel(Parcel source) {
            return new StrainDataModel(source);
        }

        @Override
        public StrainDataModel[] newArray(int size) {
            return new StrainDataModel[size];
        }
    };
}
