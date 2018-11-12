package com.codingpixel.healingbudz.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfileQATabExpandAbleData implements Parcelable {

    private  int id ;
    private int user_id;
    private String heading;
    private String discription;
    private String created_data;
    private int show_ads;

    public int getShow_ads() {
        return show_ads;
    }

    public String getCreated_data() {
        return created_data;
    }

    public void setCreated_data(String dreated_data) {
        this.created_data = dreated_data;
    }

    public UserProfileQATabExpandAbleData(){
      //default coustructor
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

    public UserProfileQATabExpandAbleData(Parcel in) {
        heading = in.readString();
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfileQATabExpandAbleData)) return false;

        UserProfileQATabExpandAbleData artist = (UserProfileQATabExpandAbleData) o;

        return getHeading() != null ? getHeading().equals(artist.getHeading()) : artist.getHeading() == null;

    }

    @Override
    public int hashCode() {
        int result = getHeading() != null ? getHeading().hashCode() : 0;
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(heading);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserProfileQATabExpandAbleData> CREATOR = new Creator<UserProfileQATabExpandAbleData>() {
        @Override
        public UserProfileQATabExpandAbleData createFromParcel(Parcel in) {
            return new UserProfileQATabExpandAbleData(in);
        }

        @Override
        public UserProfileQATabExpandAbleData[] newArray(int size) {
            return new UserProfileQATabExpandAbleData[size];
        }
    };

    public void setShow_ads(int show_ads) {
        this.show_ads = show_ads;
    }
}

