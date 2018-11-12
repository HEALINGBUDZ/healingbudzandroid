package com.codingpixel.healingbudz.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jawadali on 12/19/17.
 */
public class BudzMapReviews implements Parcelable {
    int id;
    int strain_id;
    int reviewed_by;
    String review;
    String created_at;
    String updated_at;
    int user_id;
    String user_first_name;
    String user_image_path;
    String user_avatar;
    String special_icon;
    double rating;
    //budz
    BudzMapHomeDataModel bud;
    StrainDataModel get_strain;
    int total_review;
    ReplyBudz reply;
    int user_point;
    boolean isLiked;

    public int getTotal_review() {
        return total_review;
    }

    public void setTotal_review(int total_review) {
        this.total_review = total_review;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getUser_point() {
        return user_point;
    }

    public void setUser_point(int user_point) {
        this.user_point = user_point;
    }

    public String getSpecial_icon() {
        if (special_icon != null && special_icon.length() > 5) {
            return special_icon;
        } else {
            return "";
        }
    }


    public void setSpecial_icon(String special_icon) {
        this.special_icon = special_icon;
    }

    public ReplyBudz getReply() {
        return reply;
    }

    public void setReply(ReplyBudz reply) {
        this.reply = reply;
    }

    public int getIs_user_flaged_count() {
        return is_user_flaged_count;
    }

    public void setIs_user_flaged_count(int is_user_flaged_count) {
        this.is_user_flaged_count = is_user_flaged_count;
    }

    public BudzMapHomeDataModel getBud() {
        return bud;
    }

    public void setBud(BudzMapHomeDataModel bud) {
        this.bud = bud;
    }

    public StrainDataModel getGet_strain() {
        return get_strain;
    }

    public void setGet_strain(StrainDataModel get_strain) {
        this.get_strain = get_strain;
    }

    int is_user_flaged_count;
    String attatchment_type;
    String attatchment_poster;
    String attatchment_path;

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

    public int getReviewed_by() {
        return reviewed_by;
    }

    public void setReviewed_by(int reviewed_by) {
        this.reviewed_by = reviewed_by;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getRating() {
//        if(rating!=null){
        return (int) rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAttatchment_type() {
        return attatchment_type;
    }

    public void setAttatchment_type(String attatchment_type) {
        this.attatchment_type = attatchment_type;
    }

    public String getAttatchment_poster() {
        return attatchment_poster;
    }

    public void setAttatchment_poster(String attatchment_poster) {
        this.attatchment_poster = attatchment_poster;
    }

    public String getAttatchment_path() {
        return attatchment_path;
    }

    public void setAttatchment_path(String attatchment_path) {
        this.attatchment_path = attatchment_path;
    }


    public BudzMapReviews() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.strain_id);
        dest.writeInt(this.reviewed_by);
        dest.writeString(this.review);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeInt(this.user_id);
        dest.writeString(this.user_first_name);
        dest.writeString(this.user_image_path);
        dest.writeString(this.user_avatar);
        dest.writeString(this.special_icon);
        dest.writeDouble(this.rating);
        dest.writeParcelable(this.bud, flags);
        dest.writeParcelable(this.get_strain, flags);
        dest.writeParcelable(this.reply, flags);
        dest.writeInt(this.is_user_flaged_count);
        dest.writeString(this.attatchment_type);
        dest.writeString(this.attatchment_poster);
        dest.writeString(this.attatchment_path);
        dest.writeInt(this.user_point);
        dest.writeInt(this.total_review);
    }

    protected BudzMapReviews(Parcel in) {
        this.id = in.readInt();
        this.strain_id = in.readInt();
        this.reviewed_by = in.readInt();
        this.review = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.user_id = in.readInt();
        this.user_first_name = in.readString();
        this.user_image_path = in.readString();
        this.user_avatar = in.readString();
        this.special_icon = in.readString();
        this.rating = in.readDouble();
        this.bud = in.readParcelable(BudzMapHomeDataModel.class.getClassLoader());
        this.get_strain = in.readParcelable(StrainDataModel.class.getClassLoader());
        this.reply = in.readParcelable(ReplyBudz.class.getClassLoader());
        this.is_user_flaged_count = in.readInt();
        this.attatchment_type = in.readString();
        this.attatchment_poster = in.readString();
        this.attatchment_path = in.readString();
        this.user_point = in.readInt();
        this.total_review = in.readInt();
    }

    public static final Creator<BudzMapReviews> CREATOR = new Creator<BudzMapReviews>() {
        @Override
        public BudzMapReviews createFromParcel(Parcel source) {
            return new BudzMapReviews(source);
        }

        @Override
        public BudzMapReviews[] newArray(int size) {
            return new BudzMapReviews[size];
        }
    };
}