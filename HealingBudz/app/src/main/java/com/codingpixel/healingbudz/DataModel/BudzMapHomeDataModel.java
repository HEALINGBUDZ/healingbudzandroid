package com.codingpixel.healingbudz.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class BudzMapHomeDataModel implements Parcelable {
    int id;
    int user_id;
    int business_type_id;
    String title;
    String logo;
    boolean isFlagged;
    String banner;
    int is_organic;
    int is_delivery;
    int is_featured;
    String description;
    String location;
    double lat;
    double lng;
    String phone;
    String web;
    String facebook;
    String twitter;
    String instagram;
    String email;
    String created_at;
    String updated_at;
    String stripe_id;
    int get_user_save_count;
    String card_brand;
    String card_last_four;
    String trial_ends_at;
    double distance;
    double rating_sum;
    ArrayList<Reviews> reviews;
    ArrayList<Images> images;
    Timing Timings;
    boolean isPending;
    String subScriptionName;
    boolean isCanceled;
    String endTime;
    String banner_full;
    String others_image;

    public String getOthers_image() {
        return others_image;
    }

    public void setOthers_image(String others_image) {
        this.others_image = others_image;
    }

    public String getBanner_full() {
        return banner_full;
    }

    public void setBanner_full(String banner_full) {
        this.banner_full = banner_full;
    }

    List<BudzMapSpecialProducts> specialProducts;

    public List<BudzMapSpecialProducts> getSpecialProducts() {
        if (specialProducts == null) {
            return new ArrayList<>();
        }
        return specialProducts;
    }

    public void setSpecialProducts(List<BudzMapSpecialProducts> specialProducts) {
        this.specialProducts = specialProducts;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public String
    getSubScriptionName() {
        return subScriptionName;
    }

    public void setSubScriptionName(String subScriptionName) {
        this.subScriptionName = subScriptionName;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public int getGet_user_save_count() {

        return get_user_save_count;
    }

    public void setGet_user_save_count(int get_user_save_count) {
        this.get_user_save_count = get_user_save_count;
    }

    public Timing getTimings() {
        return Timings;
    }

    public void setTimings(Timing timings) {
        Timings = timings;
    }

    public int getIs_featured() {
        return is_featured;
    }

    public void setIs_featured(int is_featured) {
        this.is_featured = is_featured;
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

    public int getBusiness_type_id() {
        return business_type_id;
    }

    public void setBusiness_type_id(int business_type_id) {
        this.business_type_id = business_type_id;
    }

    public String getTitle() {
        if (title != null) {
            return title;
        }
        return "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        if (logo != null) {
            return logo;
        }
        return "";

    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getIs_organic() {
        return is_organic;
    }

    public void setIs_organic(int is_organic) {
        this.is_organic = is_organic;
    }

    public int getIs_delivery() {
        return is_delivery;
    }

    public void setIs_delivery(int is_delivery) {
        this.is_delivery = is_delivery;
    }

    public String getDescription() {
        if (description != null)
            return description;
        return "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        if (location != null)
            return location;
        return "";
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLat() {

        return lat;

    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
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

    public String getStripe_id() {
        return stripe_id;
    }

    public void setStripe_id(String stripe_id) {
        this.stripe_id = stripe_id;
    }

    public String getCard_brand() {
        return card_brand;
    }

    public void setCard_brand(String card_brand) {
        this.card_brand = card_brand;
    }

    public String getCard_last_four() {
        return card_last_four;
    }

    public void setCard_last_four(String card_last_four) {
        this.card_last_four = card_last_four;
    }

    public String getTrial_ends_at() {
        return trial_ends_at;
    }

    public void setTrial_ends_at(String trial_ends_at) {
        this.trial_ends_at = trial_ends_at;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getRating_sum() {
        return rating_sum;
    }

    public void setRating_sum(double rating_sum) {
        this.rating_sum = rating_sum;
    }

    public ArrayList<Reviews> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Reviews> reviews) {
        this.reviews = reviews;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }


    public static class Reviews implements Parcelable {
        int id;
        int sub_user_id;
        int reviewed_by;
        String text;
        int rating;
        String created_at;
        String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSub_user_id() {
            return sub_user_id;
        }

        public void setSub_user_id(int sub_user_id) {
            this.sub_user_id = sub_user_id;
        }

        public int getReviewed_by() {
            return reviewed_by;
        }

        public void setReviewed_by(int reviewed_by) {
            this.reviewed_by = reviewed_by;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
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


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.sub_user_id);
            dest.writeInt(this.reviewed_by);
            dest.writeString(this.text);
            dest.writeInt(this.rating);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
        }

        public Reviews() {
        }

        protected Reviews(Parcel in) {
            this.id = in.readInt();
            this.sub_user_id = in.readInt();
            this.reviewed_by = in.readInt();
            this.text = in.readString();
            this.rating = in.readInt();
            this.created_at = in.readString();
            this.updated_at = in.readString();
        }

        public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
            @Override
            public Reviews createFromParcel(Parcel source) {
                return new Reviews(source);
            }

            @Override
            public Reviews[] newArray(int size) {
                return new Reviews[size];
            }
        };
    }

    public static class Timing implements Parcelable {
        int id;
        int sub_user_id;
        String monday;
        String tuesday;
        String wednesday;
        String thursday;
        String friday;
        String saturday;
        String sunday;
        String mon_end;
        String tue_end;
        String wed_end;
        String thu_end;
        String fri_end;
        String sat_end;
        String sun_end;
        String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSub_user_id() {
            return sub_user_id;
        }

        public void setSub_user_id(int sub_user_id) {
            this.sub_user_id = sub_user_id;
        }

        public String getMonday() {
            return monday;
        }

        public void setMonday(String monday) {
            this.monday = monday;
        }

        public String getTuesday() {
            return tuesday;
        }

        public void setTuesday(String tuesday) {
            this.tuesday = tuesday;
        }

        public String getWednesday() {
            return wednesday;
        }

        public void setWednesday(String wednesday) {
            this.wednesday = wednesday;
        }

        public String getThursday() {
            return thursday;
        }

        public void setThursday(String thursday) {
            this.thursday = thursday;
        }

        public String getFriday() {
            return friday;
        }

        public void setFriday(String friday) {
            this.friday = friday;
        }

        public String getSaturday() {
            return saturday;
        }

        public void setSaturday(String saturday) {
            this.saturday = saturday;
        }

        public String getSunday() {
            return sunday;
        }

        public void setSunday(String sunday) {
            this.sunday = sunday;
        }

        public String getMon_end() {
            return mon_end;
        }

        public void setMon_end(String mon_end) {
            this.mon_end = mon_end;
        }

        public String getTue_end() {
            return tue_end;
        }

        public void setTue_end(String tue_end) {
            this.tue_end = tue_end;
        }

        public String getWed_end() {
            return wed_end;
        }

        public void setWed_end(String wed_end) {
            this.wed_end = wed_end;
        }

        public String getThu_end() {
            return thu_end;
        }

        public void setThu_end(String thu_end) {
            this.thu_end = thu_end;
        }

        public String getFri_end() {
            return fri_end;
        }

        public void setFri_end(String fri_end) {
            this.fri_end = fri_end;
        }

        public String getSat_end() {
            return sat_end;
        }

        public void setSat_end(String sat_end) {
            this.sat_end = sat_end;
        }

        public String getSun_end() {
            return sun_end;
        }

        public void setSun_end(String sun_end) {
            this.sun_end = sun_end;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.sub_user_id);
            dest.writeString(this.monday);
            dest.writeString(this.tuesday);
            dest.writeString(this.wednesday);
            dest.writeString(this.thursday);
            dest.writeString(this.friday);
            dest.writeString(this.saturday);
            dest.writeString(this.sunday);
            dest.writeString(this.mon_end);
            dest.writeString(this.tue_end);
            dest.writeString(this.wed_end);
            dest.writeString(this.thu_end);
            dest.writeString(this.fri_end);
            dest.writeString(this.sat_end);
            dest.writeString(this.sun_end);
            dest.writeString(this.created_at);
        }

        public Timing() {
        }

        protected Timing(Parcel in) {
            this.id = in.readInt();
            this.sub_user_id = in.readInt();
            this.monday = in.readString();
            this.tuesday = in.readString();
            this.wednesday = in.readString();
            this.thursday = in.readString();
            this.friday = in.readString();
            this.saturday = in.readString();
            this.sunday = in.readString();
            this.mon_end = in.readString();
            this.tue_end = in.readString();
            this.wed_end = in.readString();
            this.thu_end = in.readString();
            this.fri_end = in.readString();
            this.sat_end = in.readString();
            this.sun_end = in.readString();
            this.created_at = in.readString();
        }

        public static final Creator<Timing> CREATOR = new Creator<Timing>() {
            @Override
            public Timing createFromParcel(Parcel source) {
                return new Timing(source);
            }

            @Override
            public Timing[] newArray(int size) {
                return new Timing[size];
            }
        };
    }

    public static class Images {
        int id;
        int strain_id;
        int user_id;
        String image_path;
        int is_approved;
        int is_main;
        String Name;
        String created_at;
        String updated_at;
        boolean isLocal;
        String pathLocal;

        public String getPathLocal() {
            return pathLocal;
        }

        public void setPathLocal(String pathLocal) {
            this.pathLocal = pathLocal;
        }

        public Images() {
            isLocal = false;
        }

        public boolean isLocal() {
            return isLocal;
        }

        public void setLocal(boolean local) {
            isLocal = local;
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
    }

    public BudzMapHomeDataModel() {
        isPending = false;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.user_id);
        dest.writeInt(this.business_type_id);
        dest.writeString(this.title);
        dest.writeString(this.logo);
        dest.writeByte(this.isFlagged ? (byte) 1 : (byte) 0);
        dest.writeString(this.banner);
        dest.writeInt(this.is_organic);
        dest.writeInt(this.is_delivery);
        dest.writeInt(this.is_featured);
        dest.writeString(this.description);
        dest.writeString(this.location);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.phone);
        dest.writeString(this.web);
        dest.writeString(this.facebook);
        dest.writeString(this.twitter);
        dest.writeString(this.instagram);
        dest.writeString(this.email);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.stripe_id);
        dest.writeInt(this.get_user_save_count);
        dest.writeString(this.card_brand);
        dest.writeString(this.card_last_four);
        dest.writeString(this.trial_ends_at);
        dest.writeDouble(this.distance);
        dest.writeDouble(this.rating_sum);
        dest.writeTypedList(this.reviews);
        dest.writeList(this.images);
        dest.writeParcelable(this.Timings, flags);
        dest.writeByte(this.isPending ? (byte) 1 : (byte) 0);
        dest.writeString(this.subScriptionName);
        dest.writeString(this.endTime);
        dest.writeByte(this.isCanceled ? (byte) 1 : (byte) 0);
    }

    protected BudzMapHomeDataModel(Parcel in) {
        this.id = in.readInt();
        this.user_id = in.readInt();
        this.business_type_id = in.readInt();
        this.title = in.readString();
        this.logo = in.readString();
        this.isFlagged = in.readByte() != 0;
        this.banner = in.readString();
        this.is_organic = in.readInt();
        this.is_delivery = in.readInt();
        this.is_featured = in.readInt();
        this.description = in.readString();
        this.location = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.phone = in.readString();
        this.web = in.readString();
        this.facebook = in.readString();
        this.twitter = in.readString();
        this.instagram = in.readString();
        this.email = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.stripe_id = in.readString();
        this.get_user_save_count = in.readInt();
        this.card_brand = in.readString();
        this.card_last_four = in.readString();
        this.trial_ends_at = in.readString();
        this.distance = in.readDouble();
        this.rating_sum = in.readDouble();
        this.reviews = in.createTypedArrayList(Reviews.CREATOR);
        this.images = new ArrayList<Images>();
        in.readList(this.images, Images.class.getClassLoader());
        this.Timings = in.readParcelable(Timing.class.getClassLoader());
        this.isPending = in.readByte() != 0;
        this.subScriptionName = in.readString();
        this.endTime = in.readString();
        this.isCanceled = in.readByte() != 0;
    }

    public static final Creator<BudzMapHomeDataModel> CREATOR = new Creator<BudzMapHomeDataModel>() {
        @Override
        public BudzMapHomeDataModel createFromParcel(Parcel source) {
            return new BudzMapHomeDataModel(source);
        }

        @Override
        public BudzMapHomeDataModel[] newArray(int size) {
            return new BudzMapHomeDataModel[size];
        }
    };
}
