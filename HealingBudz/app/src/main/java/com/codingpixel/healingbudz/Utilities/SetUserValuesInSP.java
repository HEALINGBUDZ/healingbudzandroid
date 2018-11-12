package com.codingpixel.healingbudz.Utilities;

import android.content.Context;

import com.codingpixel.healingbudz.DataModel.User;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

/**
 * Created by jawadali on 10/19/17.
 */

public class SetUserValuesInSP {
    public static void save_UserValues(User userValues, Context context) {
        SharedPrefrences.setInt("id", userValues.getId(), context);
        SharedPrefrences.setInt("user_id", userValues.getUser_id(), context);
        SharedPrefrences.setString("device_type", userValues.getDevice_type(), context);
        SharedPrefrences.setString("device_id", userValues.getDevice_id(), context);
        SharedPrefrences.setString("special_icon", userValues.getSpecial_icon(), context);
        SharedPrefrences.setString("lat", userValues.getLat() + "", context);
        SharedPrefrences.setString("lng", userValues.getLng() + "", context);
        SharedPrefrences.setString("session_key", userValues.getSession_key(), context);
        SharedPrefrences.setString("time_zone", userValues.getTime_zone(), context);
        SharedPrefrences.setString("fb_id", userValues.getFb_id(), context);
        SharedPrefrences.setString("g_id", userValues.getG_id(), context);
        SharedPrefrences.setString("created_at", userValues.getCreated_at(), context);
        SharedPrefrences.setString("updated_at", userValues.getUpdated_at(), context);
        SharedPrefrences.setString("first_name", userValues.getFirst_name(), context);
        SharedPrefrences.setString("last_name", userValues.getLast_name(), context);
        SharedPrefrences.setString("email", userValues.getEmail(), context);
        SharedPrefrences.setString("zip_code", userValues.getZip_code(), context);
        SharedPrefrences.setString("image_path", userValues.getImage_path(), context);
        SharedPrefrences.setString("user_type", userValues.getUser_type(), context);
        SharedPrefrences.setString("avatar", userValues.getAvatar(), context);
        SharedPrefrences.setString("cover", userValues.getCover(), context);
        SharedPrefrences.setString("bio", userValues.getBio(), context);
        SharedPrefrences.setString("location", userValues.getLocation(), context);
        SharedPrefrences.setInt("points", userValues.getPoints(), context);
    }

    public static void delete_UserValues(Context context) {
        SharedPrefrences.setInt("id", 0, context);
        SharedPrefrences.setInt("user_id", 0, context);
        SharedPrefrences.setString("device_type", "", context);
        SharedPrefrences.setString("device_id", "", context);
        SharedPrefrences.setString("lat", "", context);
        SharedPrefrences.setString("lng", "", context);
        SharedPrefrences.setString("session_key", "", context);
        SharedPrefrences.setString("time_zone", "", context);
        SharedPrefrences.setString("fb_id", "", context);
        SharedPrefrences.setString("g_id", "", context);
        SharedPrefrences.setString("created_at", "", context);
        SharedPrefrences.setString("updated_at", "", context);
        SharedPrefrences.setString("first_name", "", context);
        SharedPrefrences.setString("last_name", "", context);
        SharedPrefrences.setString("email", "", context);
        SharedPrefrences.setString("special_icon", "", context);
        SharedPrefrences.setString("zip_code", "", context);
        SharedPrefrences.setString("image_path", "", context);
        SharedPrefrences.setString("user_type", "", context);
        SharedPrefrences.setString("avatar", "", context);
        SharedPrefrences.setString("cover", "", context);
        SharedPrefrences.setString("bio", "", context);
        SharedPrefrences.setString("location", "", context);
        SharedPrefrences.setInt("points", 0, context);
    }


    public static User getSavedUser(Context context) {
        User user = new User();
        user.setId(SharedPrefrences.getInt("id", context));
        user.setUser_id(SharedPrefrences.getInt("user_id", context));
        user.setDevice_type(SharedPrefrences.getString("device_type", context));
        user.setDevice_id(SharedPrefrences.getString("device_id", context));
        user.setLat(Double.parseDouble(SharedPrefrences.getString("lat", context)));
        user.setLng(Double.parseDouble(SharedPrefrences.getString("lng", context)));
        user.setSession_key(SharedPrefrences.getString("session_key", context));
        user.setTime_zone(SharedPrefrences.getString("time_zone", context));
        user.setFb_id(SharedPrefrences.getString("fb_id", context));
        user.setG_id(SharedPrefrences.getString("g_id", context));
        user.setCreated_at(SharedPrefrences.getString("created_at", context));
        user.setUpdated_at(SharedPrefrences.getString("updated_at", context));
        user.setFirst_name(SharedPrefrences.getString("first_name", context));
        user.setLast_name(SharedPrefrences.getString("last_name", context));
        user.setEmail(SharedPrefrences.getString("email", context));
        user.setZip_code(SharedPrefrences.getString("zip_code", context));
        user.setImage_path(SharedPrefrences.getString("image_path", context));
        user.setUser_type(SharedPrefrences.getString("user_type", context));
        user.setAvatar(SharedPrefrences.getString("avatar", context));
        user.setCover(SharedPrefrences.getString("cover", context));
        user.setSpecial_icon(SharedPrefrences.getString("special_icon", context));
        user.setBio(SharedPrefrences.getString("bio", context));
        user.setLocation(SharedPrefrences.getString("location", context));
        user.setPoints(SharedPrefrences.getInt("points", context));
        return user;
    }
}
