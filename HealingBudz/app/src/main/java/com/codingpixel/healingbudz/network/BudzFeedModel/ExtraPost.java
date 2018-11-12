package com.codingpixel.healingbudz.network.BudzFeedModel;
/*
 * Created by M_Muzammil Sharif on 15-Mar-18.
 */

import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.Post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExtraPost implements Serializable {
    private List<Post> posts;

    public ExtraPost() {
        posts = new ArrayList<>();
    }

    public List<Post> getPosts() {
        if(posts == null){
            posts = new ArrayList<>();
        }
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
