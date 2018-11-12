package com.codingpixel.healingbudz.network.BudzFeedModel.SubUsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
 * Created by M_Muzammil Sharif on 02-Apr-18.
 */
public class SuccessData {
    @SerializedName("sub_users")
    @Expose
    private List<SubUser> subUsers = null;

    public List<SubUser> getSubUsers() {
        return subUsers;
    }

    public void setSubUsers(List<SubUser> subUsers) {
        this.subUsers = subUsers;
    }
}
