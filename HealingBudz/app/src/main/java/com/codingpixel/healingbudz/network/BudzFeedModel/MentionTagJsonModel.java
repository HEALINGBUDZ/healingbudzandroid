package com.codingpixel.healingbudz.network.BudzFeedModel;
/*
 * Created by M_Muzammil Sharif on 26-Mar-18.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.linkedin.android.spyglass.mentions.Mentionable;

import java.io.Serializable;

public class MentionTagJsonModel implements Mentionable, Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("trigger")
    @Expose
    private String trigger;

    public MentionTagJsonModel(String id, boolean isMention, String value) {
        this.id = id;
        this.type = isMention ? "user" : "tag";
        this.value = value;
        this.trigger = isMention ? "@" : "#";
    }

    public MentionTagJsonModel(String id, String type, String value, String trigger) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.trigger = trigger;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    @NonNull
    @Override
    public String getTextForDisplayMode(MentionDisplayMode mode) {
        switch (mode) {
            case FULL:
                return getValue();
            case PARTIAL:
            case NONE:
            default:
                return "";
        }
    }

    @Override
    public MentionDeleteStyle getDeleteStyle() {
        return MentionDeleteStyle.PARTIAL_NAME_DELETE;
    }

    @Override
    public int getSuggestibleId() {
        return Integer.parseInt(id);
    }

    @Override
    public String getSuggestiblePrimaryText() {

        return getValue();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getValue());
    }

    public MentionTagJsonModel(Parcel in) {
        value = in.readString();
    }

    public static final Parcelable.Creator<MentionTagJsonModel> CREATOR
            = new Parcelable.Creator<MentionTagJsonModel>() {
        public MentionTagJsonModel createFromParcel(Parcel in) {
            return new MentionTagJsonModel(in);
        }

        public MentionTagJsonModel[] newArray(int size) {
            return new MentionTagJsonModel[size];
        }
    };
}
