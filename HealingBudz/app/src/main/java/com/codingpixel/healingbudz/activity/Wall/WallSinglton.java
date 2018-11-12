package com.codingpixel.healingbudz.activity.Wall;
/*
 * Created by M_Muzammil Sharif on 22-Mar-18.
 */

import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.TagModel.Tag;

import java.util.ArrayList;
import java.util.List;

public class WallSinglton {
    private static WallSinglton instance;

    private List<FollowingUser> followingUserList;
    private List<Tag> hashTagList;

    public static WallSinglton getInstance() {
        if (instance == null) {
            instance = new WallSinglton();
        }
        return instance;
    }

    public List<FollowingUser> getFollowingUserList() {
        if (followingUserList == null) {
            followingUserList = new ArrayList<>();
        }
        return followingUserList;
    }

    public void setFollowingUserList(List<FollowingUser> followingUserList) {
        this.followingUserList = followingUserList;
    }

    public List<Tag> getHashTagList() {
        if (hashTagList == null) {
            hashTagList = new ArrayList<>();
        }
        return hashTagList;
    }

    public void setHashTagList(List<Tag> hashTagList) {
        this.hashTagList = hashTagList;
    }
}
