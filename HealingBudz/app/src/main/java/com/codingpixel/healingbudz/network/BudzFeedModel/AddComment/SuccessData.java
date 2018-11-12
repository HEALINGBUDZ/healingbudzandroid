
package com.codingpixel.healingbudz.network.BudzFeedModel.AddComment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessData {

    @SerializedName("comments")
    @Expose
    private Comment comments = null;

    public Comment getComments() {
        return comments;
    }

    public void setComments(Comment comments) {
        this.comments = comments;
    }

}
